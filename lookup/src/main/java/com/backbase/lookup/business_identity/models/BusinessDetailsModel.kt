package com.backbase.lookup.business_identity.models

data class BusinessDetailsModel(
    val legalName: String,
    val knownName: String,
    val ein: Int?,
    val establishedDate: String,
    val operationState: String
)