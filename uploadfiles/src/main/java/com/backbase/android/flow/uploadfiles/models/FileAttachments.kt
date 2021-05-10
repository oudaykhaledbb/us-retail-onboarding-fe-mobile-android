package com.backbase.android.flow.uploadfiles.models

data class FileAttachments(
    val documentRequest: DocumentRequest,
    var files: ArrayList<File>?,
)