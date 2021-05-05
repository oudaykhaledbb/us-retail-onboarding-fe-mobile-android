package com.backbase.android.flow.uploadfiles.models

import com.google.gson.annotations.SerializedName

data class LoadDocumentModel(
    @SerializedName("internalId")
    val internalId: String,
    @SerializedName("tempGroupId")
    val tempGroupId: String
)