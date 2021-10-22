package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class DocumentsDataModel(
    @SerializedName("comments")
    val comments: List<Comment>,
    @SerializedName("fileset")
    val fileset: Fileset
)