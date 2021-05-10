package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class RequestedDocumentsModel(
    @SerializedName("business")
    val business: Business,
    @SerializedName("documentRequests")
    val documentRequests: List<DocumentRequest>,
    @SerializedName("emailVerified")
    val emailVerified: Boolean,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("phoneNumberVerified")
    val phoneNumberVerified: Boolean,
    @SerializedName("registrar")
    val registrar: Registrar,
    @SerializedName("termsAndConditions")
    val termsAndConditions: TermsAndConditions
)