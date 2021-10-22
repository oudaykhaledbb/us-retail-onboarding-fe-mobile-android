package com.backbase.lookup.business_identity.models


import com.google.gson.annotations.SerializedName

data class IndustryCollectionModel(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String
)