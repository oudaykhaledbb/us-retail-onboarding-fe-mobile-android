package com.backbase.android.flow.otp.models

import com.google.gson.annotations.SerializedName

data class OtpModel(
    @SerializedName("channel")
    val channel: String,
    @SerializedName("otp")
    val otp: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null
)