package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class File(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastModifiedAt")
    val lastModifiedAt: String? = null,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("size")
    val size: Int? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("tempGroupId")
    val tempGroupId: String
)