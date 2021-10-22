package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class CompanyDetailsModel(
    @SerializedName("jurisdictionCode")
    val jurisdictionCode: String?,
    @SerializedName("number")
    val number: String
)