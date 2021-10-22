package com.backbase.android.flow.productselector.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.productselector.ProductSelectorConfiguration
import com.backbase.android.flow.productselector.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_product_bottom_sheet.*
import org.koin.android.ext.android.inject
import java.util.*

class ProductBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var product: Product
    private var isSelected: Boolean = false
    private val config: ProductSelectorConfiguration by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        productFeatureView?.addFeatures(product.benefits)
        lblProductDescription?.text = product.description
        lblHeader?.text = product.name
        lblPeriod.text = product.costsFrequency.toLowerCase(Locale.ROOT)
        lblPrice.text = "${product.cost.currency} ${product.cost.value}"
        btnContinue.text = getString(if (isSelected) R.string.product_selection_journey_unselect_product else R.string.product_selection_journey_select_product )
        btnContinue.setOnClickListener {
            (requireParentFragment() as OnProductSelectedListener).onProductSelected(product, !isSelected)
            dismissAllowingStateLoss()
        }
        var requestOptions = RequestOptions()
        Glide.with(requireContext())
            .load(product.imageUrl.replace("{BB_GATEWAY_URL}", config.imageBaseUrl))
            .apply(requestOptions)
            .into(imgProduct)
    }

    companion object{
        fun newInstance(
            product: Product,
            isSelected: Boolean
        ): ProductBottomSheetFragment {
            val fragment = ProductBottomSheetFragment()
            fragment.isSelected = isSelected
            fragment.product = product
            return fragment
        }
    }
}

interface OnProductSelectedListener{
    fun onProductSelected(
        product: Product,
        isSelected: Boolean
    )
}