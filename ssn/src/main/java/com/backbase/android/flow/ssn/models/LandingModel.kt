package com.backbase.android.flow.ssn.models


import com.google.gson.annotations.SerializedName

data class LandingModel(
    @SerializedName("caseId")
    val caseId: String,
    @SerializedName("email")
    val email: String
)