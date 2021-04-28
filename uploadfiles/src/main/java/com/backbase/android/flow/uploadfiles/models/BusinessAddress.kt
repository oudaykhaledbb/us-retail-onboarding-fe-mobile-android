package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class BusinessAddress(
    @SerializedName("apt")
    val apt: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("numberAndStreet")
    val numberAndStreet: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zipCode")
    val zipCode: String
)