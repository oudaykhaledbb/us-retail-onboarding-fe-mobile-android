package com.backbase.android.flow.productselector

import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.v2.models.InteractionResponse

/**
 * Created by Backbase R&D B.V. on 2020-11-17.
 *
 */
interface ProductSelectorUseCase {
    suspend fun createCase(): Any?
    suspend fun requestProducts(): InteractionResponse<List<Product>>?
    suspend fun submitProduct(selectedProducts: ArrayList<Product>): Any?
}