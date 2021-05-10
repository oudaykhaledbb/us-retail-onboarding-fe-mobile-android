package com.backbase.android.flow.ssn.models

import com.google.gson.annotations.SerializedName

data class SsnRequest(
    @SerializedName("ssn")
    val ssn: String
)