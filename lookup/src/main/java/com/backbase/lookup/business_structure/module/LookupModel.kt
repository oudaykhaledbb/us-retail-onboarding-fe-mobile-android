package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class LookupModel(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("jurisdictionCode")
    val jurisdictionCode: String?,
    @SerializedName("name")
    val name: String
)