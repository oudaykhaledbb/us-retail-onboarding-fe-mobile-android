package com.backbase.android.flow.productselector

import com.backbase.android.flow.models.Action
import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class ProductSelectorUseCaseDefaultImpl(
    private val flowClient: FlowClientContract,
    private val productSelectorConfiguration: ProductSelectorConfiguration
) : ProductSelectorUseCase {


    override suspend fun createCase(): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction(
            Action(productSelectorConfiguration.createCaseAction, null), responseType)
    }


    override suspend fun requestProducts(): InteractionResponse<List<Product>>? {
        val responseType: Type =
            object : TypeToken<List<Product>?>() {}.type
        return flowClient.performInteraction(
            Action(productSelectorConfiguration.requestProductsAction, null), responseType)
    }

    override suspend fun submitProduct(selectedProducts: ArrayList<Product>): InteractionResponse<Any?>? {
        val request = HashMap<String, ArrayList<HashMap<String, String>>>()
        val productsHashmap = ArrayList<HashMap<String, String>>()
        selectedProducts.forEach {product ->
            productsHashmap.add(hashMapOf(
                "referenceId" to product.referenceId,
                "productName" to product.name
            ))
        }
        request["selection"] = productsHashmap
        val responseType: Type =
            object : TypeToken<Any?>() {}.type
        return flowClient.performInteraction(
            Action(productSelectorConfiguration.submitProductsAction, request), responseType)
    }

}