package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("creatorId")
    val creatorId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastModifiedAt")
    val lastModifiedAt: Any,
    @SerializedName("parentId")
    val parentId: Any
)