package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class DocumentRequest(
    @SerializedName("deadline")
    val deadline: String,
    @SerializedName("documentType")
    val documentType: String,
    @SerializedName("externalId")
    val externalId: String,
    @SerializedName("filesetName")
    val filesetName: String,
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("initialNote")
    val initialNote: String,
    @SerializedName("internalId")
    val internalId: String,
    @SerializedName("processInstanceId")
    val processInstanceId: String,
    @SerializedName("status")
    val status: String
)