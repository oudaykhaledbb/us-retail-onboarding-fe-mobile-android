package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("isoCode")
    val isoCode: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("states")
    val states: List<State>,
    @SerializedName("synonyms")
    val synonyms: List<String>
)