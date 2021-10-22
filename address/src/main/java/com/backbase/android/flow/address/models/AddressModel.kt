package com.backbase.android.flow.address.models

import com.google.gson.annotations.SerializedName

data class AddressModel(
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
) {
    override fun toString() =
        String.format(
            "%s %s, %s %s %s",
            apt,
            numberAndStreet,
            zipCode,
            city,
            state
        )
}
