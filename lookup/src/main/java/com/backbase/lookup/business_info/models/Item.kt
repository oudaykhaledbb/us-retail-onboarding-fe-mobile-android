package com.backbase.lookup.business_info.models


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("subtypes")
    val subtypes: List<String>,
    @SerializedName("type")
    val type: String
)