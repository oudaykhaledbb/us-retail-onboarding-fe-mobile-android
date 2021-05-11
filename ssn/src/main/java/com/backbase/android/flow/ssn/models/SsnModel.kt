package com.backbase.android.flow.ssn.models

import com.google.gson.annotations.SerializedName

data class SsnModel(
    @SerializedName("ssn")
    val ssn: String
)