package com.backbase.android.flow.smeo.business.info.models

data class BusinessDetailsModel(
    val legalName: String,
    val knownName: String,
    val ein: Int?,
    val establishedDate: String,
    val operationState: String
)