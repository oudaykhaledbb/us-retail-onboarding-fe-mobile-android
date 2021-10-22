package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class State(
    @SerializedName("isoCode")
    val isoCode: String,
    @SerializedName("name")
    val name: String
)