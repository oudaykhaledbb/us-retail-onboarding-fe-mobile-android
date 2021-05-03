package com.backbase.android.flow.uploadfiles.usecase

import com.backbase.android.flow.uploadfiles.models.DocumentsDataModel
import com.backbase.android.flow.uploadfiles.models.RequestedDocumentsModel
import com.backbase.android.flow.uploadfiles.models.UploadDocumentResponse
import java.io.File

interface UploadFilesUseCase {

    suspend fun requestDocumentList(): RequestedDocumentsModel?
    suspend fun requestDocumentData(): DocumentsDataModel?
    suspend fun deleteTempDocument(tempGroupId: String, internalId: String, fileId: String): Any?
    suspend fun uploadDocument(tempGroupId: String, internalId: String, file: File): UploadDocumentResponse?
    suspend fun completeTask(): Any?
    suspend fun submitDocumentRequests(): Any?

}