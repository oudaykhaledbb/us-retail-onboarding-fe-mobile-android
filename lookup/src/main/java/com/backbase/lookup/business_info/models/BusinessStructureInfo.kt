package com.backbase.lookup.business_info.models


import com.google.gson.annotations.SerializedName

data class BusinessStructureInfo(
    @SerializedName("type")
    val type: String,
    @SerializedName("subtype")
    val subtype: String?
)