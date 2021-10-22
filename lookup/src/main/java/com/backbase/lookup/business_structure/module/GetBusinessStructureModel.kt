package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class GetBusinessStructureModel(
    @SerializedName("count")
    val count: Any,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("next")
    val next: Any
)