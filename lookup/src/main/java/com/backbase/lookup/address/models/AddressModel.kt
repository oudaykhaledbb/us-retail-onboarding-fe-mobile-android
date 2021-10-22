package com.backbase.lookup.address.models

import com.google.gson.annotations.SerializedName

data class AddressModel (
    @SerializedName("numberAndStreet")
    val numberAndStreet: String?,
    @SerializedName("apt")
    val apt: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("zipCode")
    val zipCode: String?
)