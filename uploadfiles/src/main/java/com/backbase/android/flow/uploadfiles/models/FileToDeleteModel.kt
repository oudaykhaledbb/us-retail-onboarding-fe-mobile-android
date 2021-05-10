package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class FileToDeleteModel(
    @SerializedName("tempGroupId")
    val tempGroupId: String,
    @SerializedName("internalId")
    val internalId: String,
    @SerializedName("fileId")
    val fileId: String,
)