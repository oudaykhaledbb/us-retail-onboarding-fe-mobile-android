package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class Industry(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)