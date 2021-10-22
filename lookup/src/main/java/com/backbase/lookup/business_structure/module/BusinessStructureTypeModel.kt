package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class BusinessStructureTypeModel(
    @SerializedName("type")
    val type: String,
    @SerializedName("subtype")
    val subtype: String?,
)