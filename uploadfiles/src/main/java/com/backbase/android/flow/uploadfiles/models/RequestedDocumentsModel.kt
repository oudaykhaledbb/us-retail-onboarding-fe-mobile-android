package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class RequestedDocumentsModel(
    @SerializedName("documentRequests")
    val documentRequests: List<DocumentRequest>
)