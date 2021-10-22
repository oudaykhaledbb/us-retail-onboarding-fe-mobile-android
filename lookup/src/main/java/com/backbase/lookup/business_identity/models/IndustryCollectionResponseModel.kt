package com.backbase.lookup.business_identity.models


import com.backbase.lookup.business_identity.models.IndustryCollectionModel
import com.google.gson.annotations.SerializedName

data class IndustryCollectionResponseModel(
    @SerializedName("count")
    val count: Int,
    @SerializedName("items")
    val items: List<IndustryCollectionModel>,
    @SerializedName("next")
    val next: Any
)