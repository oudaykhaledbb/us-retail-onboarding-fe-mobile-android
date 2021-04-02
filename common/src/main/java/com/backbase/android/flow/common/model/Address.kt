package com.backbase.android.flow.common.model

data class Address(
    var numberAndStreet: String? = null,
    var apt: String? = null,
    var zipCode: String? = null,
    var city: String? = null,
    var state: String? = null
) {
    override fun toString() =
        String.format(
            "%s %s, %s %s %s",
            apt ?: "",
            numberAndStreet ?: "",
            zipCode ?: "",
            city ?: "",
            state ?: ""
        )
}