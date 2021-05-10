package com.backbase.android.flow.uploadfiles.models

import com.google.gson.annotations.SerializedName

data class UploadDocumentRequest(
    @SerializedName("tempGroupId")
    val tempGroupId: String,
    @SerializedName("internalId")
    val internalId: String,
)
