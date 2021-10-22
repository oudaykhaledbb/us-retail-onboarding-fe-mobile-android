package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class UploadDocumentResponse(
    @SerializedName("allowedMediaTypes")
    val allowedMediaTypes: List<String>? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("files")
    val files: List<File>,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastModifiedAt")
    val lastModifiedAt: String? = null,
    @SerializedName("maxFileSize")
    val maxFileSize: Any? = null,
    @SerializedName("maxFiles")
    val maxFiles: Int? = null,
    @SerializedName("name")
    val name: String
)