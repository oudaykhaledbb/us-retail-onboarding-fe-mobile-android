package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class Fileset(
    @SerializedName("allowedMediaTypes")
    val allowedMediaTypes: List<String>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("files")
    val files: List<File>,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastModifiedAt")
    val lastModifiedAt: String,
    @SerializedName("maxFileSize")
    val maxFileSize: Any,
    @SerializedName("maxFiles")
    val maxFiles: Int,
    @SerializedName("name")
    val name: String
)