package com.backbase.android.flow.productselector.screen

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.models.ActionError
import com.backbase.android.flow.productselector.*
import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.productselector.screen.uicomponent.ProductSelectionViewModel
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.deferredresources.DeferredText
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.screen_product_selection.*
import kotlinx.android.synthetic.main.screen_product_selection.view.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

const val JOURNEY_NAME_PRODUCT_SELECTION= "JOURNEY_NAME_PRODUCT_SELECTION"

class ProductSelectionScreen : Fragment(R.layout.screen_product_selection),
    OnProductSelectedListener {

    private lateinit var productAdapter: ProductRecyclerViewAdapter
    private val router: ProductSelectorRouter by inject()
    private val viewModel: ProductSelectionViewModel by inject()
    private val config: ProductSelectorConfiguration by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsProductSelection.PRODUCT_SELECTION.value)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStates()

        btnHelp.setOnClickListener {
            router.showHelpWhichProductToUse()
        }
        productAdapter =
            ProductRecyclerViewAdapter(config.imageBaseUrl) { _, product, isSelected ->
                showButtomSheet(product, isSelected)
                return@ProductRecyclerViewAdapter false
            }
        view.rvProduct.adapter = productAdapter

        view.rvProduct.setOrientation(DSVOrientation.HORIZONTAL)
        view.rvProduct.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        )

        view.rvProduct.setSlideOnFling(true)
        view.rvProduct.setClampTransformProgressAfter(3)
        view.rvProduct.setOffscreenItems(3)
        view.rvProduct.setOverScrollEnabled(true)


        Handler().postDelayed({ viewModel.requestProducts() }, 30)

        btnContinue.setOnClickListener { viewModel.submitProduct(productAdapter.selectedProducts) }
        view.btnHelp.visibility = if (config.hideHelperLink) View.GONE else View.VISIBLE

    }

    private fun showButtomSheet(product: Product, isSelected: Boolean) {
        ProductBottomSheetFragment.newInstance(product, isSelected)
            .show(childFragmentManager, "ProductBottomSheetFragment")
    }

    private fun blockUI() {
        rvProduct.isEnabled = false
        btnContinue.loading = true
    }

    private fun unblockUI() {
        rvProduct.isEnabled = true
        btnContinue.loading = false
        container.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


    private fun observeStates() {
        handleStateForSubmitApis(btnContinue, viewModel.apiSubmitProduct.state)
        handleStateForRequestApis(btnContinue, viewModel.apiRequestProducts.state)
    }


    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<Any?>>
    ) {
        handleStates(
            apiState,
            {
                router.onProductSelectorFinished(it)
            },
            null,
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    private fun handleStateForRequestApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<InteractionResponse<List<Product>>?>>
    ) {
        handleStates(
            apiState,
            {
                it?.body?.let { products -> drawProducts(products) }
            },
            null,
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    private fun printErrors(actionErrors: List<ActionError>?) {
        if (actionErrors == null) print("unknown error")
        actionErrors?.forEach {
            print(it.message)
        }
    }

    private fun drawProducts(products: List<Product>) {
        unblockUI()
        productAdapter.setItems(products)
        requireView().indicator.itemCount = productAdapter.pageCount
        requireView().indicator.setWithViewPager2(requireView().rvProduct, false)
    }

    override fun onProductSelected(
        product: Product,
        isSelected: Boolean
    ) {
        if (isSelected) {
            if (config.selectionType == SelectionType.SINGLE) {
                productAdapter.setSingleSelection(product)
            } else {
                productAdapter.selectProduct(product)
            }
        } else productAdapter.unselectProduct(product)
        btnContinue.text =
            String.format(getString(R.string.product_selection_journey_button_continue), productAdapter.selectedProducts.size)
        btnContinue.visibility =
            if (productAdapter.selectedProducts.size > 0) View.VISIBLE else View.GONE
    }

    companion object{
        val JOURNEY_HEADER_INFO_DEFAULT = hashMapOf(
            JourneyStepsProductSelection.PRODUCT_SELECTION.value.name to HeaderInfo(
                DeferredText.Resource(R.string.product_selection_journey_title),
                DeferredText.Resource(R.string.product_selection_journey_subtitle),
                JourneyStepsProductSelection.PRODUCT_SELECTION.value.allowBack
            )
        )
    }

}


enum class JourneyStepsProductSelection(val value: StepInfo){
    PRODUCT_SELECTION(StepInfo(JOURNEY_NAME_PRODUCT_SELECTION,  "PRODUCT_SELECTION", false))
}


