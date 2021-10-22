package com.backbase.lookup.business_identity.models


import com.google.gson.annotations.SerializedName

data class Industry(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String
)