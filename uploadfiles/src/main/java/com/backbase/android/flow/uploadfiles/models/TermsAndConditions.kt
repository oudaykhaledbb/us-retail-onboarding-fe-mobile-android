package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class TermsAndConditions(
    @SerializedName("acceptanceDate")
    val acceptanceDate: String,
    @SerializedName("accepted")
    val accepted: Boolean
)