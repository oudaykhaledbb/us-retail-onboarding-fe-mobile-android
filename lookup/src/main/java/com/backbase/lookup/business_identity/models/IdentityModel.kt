package com.backbase.lookup.business_identity.models


import com.google.gson.annotations.SerializedName

data class IdentityModel(
    @SerializedName("description")
    val description: String?,
    @SerializedName("industries")
    val industries: List<Industry>,
    @SerializedName("website")
    val website: String?
)