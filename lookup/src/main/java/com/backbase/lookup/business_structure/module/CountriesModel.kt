package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class CountriesModel(
    @SerializedName("count")
    val count: Any,
    @SerializedName("items")
    val items: List<Country>,
    @SerializedName("next")
    val next: Any
)