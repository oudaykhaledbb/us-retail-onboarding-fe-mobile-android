package com.backbase.android.flow.smeo.business.models

data class AddressModel(
    val numberAndStreet: String,
    val apt: String,
    val city: String,
    val state: String,
    val zipCode: String
)