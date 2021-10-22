package com.backbase.android.flow.productselector

import com.backbase.android.flow.productselector.models.Cost
import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.v2.models.InteractionResponse

class FakeProductSelectorUseCaseImpl:
    ProductSelectorUseCase {

    override suspend fun createCase() = null

    override suspend fun requestProducts() = InteractionResponse<List<Product>>().apply {
        (body as ArrayList<Product>).addAll(getLargeListOfProducts())
    }

    override suspend fun submitProduct(selectedProducts: ArrayList<Product>) = ""

    private fun getLargeListOfProducts(): List<Product> {
        return arrayListOf(
            generateProduct(1),
            generateProduct(2),
            generateProduct(3),
            generateProduct(4),
            generateProduct(5)
        )
    }

    private fun generateProduct(id: Int) =
        Product(
            name = "VISA Platinum $id",
            description = "Description $id",
            benefits = arrayListOf("Test 1$id", "Test 2$id", "Test 3$id"),
            imageUrl = "https://static2.hdwallpapers.net/wallpapers/2019/02/22/1176/thumb_orange-mountains.jpg",
            cost = Cost(
                "USD",
                100.0
            ),
            costsFrequency = "monthly",
            detailedProductDescriptionUrl = "",
            productType = "",
            referenceId = ""
        )

}