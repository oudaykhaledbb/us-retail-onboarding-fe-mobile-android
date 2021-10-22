package com.backbase.android.flow.productselector.screen.uicomponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.productselector.ProductSelectorConfiguration
import com.backbase.android.flow.productselector.ProductSelectorUseCase
import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.v2.models.InteractionResponse
import kotlinx.coroutines.delay

class ProductSelectionViewModel(
    val useCase: ProductSelectorUseCase,
    private val productSelectorConfiguration: ProductSelectorConfiguration
) : ViewModel() {

    internal val apiRequestProducts =
        ApiPublisher<InteractionResponse<List<Product>>?>(this.viewModelScope)

    internal val apiSubmitProduct = ApiPublisher<Any?>(this.viewModelScope)

    fun requestProducts() {
        apiRequestProducts.submit("requestProducts()") {
            if (productSelectorConfiguration.createCaseAction.isNotEmpty()) {
                useCase.createCase()
            }
            delay(30)
            return@submit useCase.requestProducts()
        }
    }

    fun submitProduct(selectedProducts: ArrayList<Product>) {
        apiSubmitProduct.submit("submitProduct()") {
            return@submit useCase.submitProduct(selectedProducts)
        }
    }
}