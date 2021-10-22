package com.backbase.android.flow.otp.models

import com.google.gson.annotations.SerializedName

data class EmailModel (
    @SerializedName("email")
    val email: String?,
)