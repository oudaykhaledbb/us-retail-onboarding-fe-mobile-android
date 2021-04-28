package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class Business(
    @SerializedName("businessAddress")
    val businessAddress: BusinessAddress,
    @SerializedName("businessType")
    val businessType: String,
    @SerializedName("ein")
    val ein: String,
    @SerializedName("establishedDate")
    val establishedDate: String,
    @SerializedName("industry")
    val industry: Industry,
    @SerializedName("knownName")
    val knownName: String,
    @SerializedName("legalName")
    val legalName: String,
    @SerializedName("nature")
    val nature: String,
    @SerializedName("operationState")
    val operationState: String,
    @SerializedName("phoneNumbers")
    val phoneNumbers: List<Any>,
    @SerializedName("website")
    val website: String
)