package com.backbase.android.flow.uploadfiles.usecase

import com.backbase.android.flow.uploadfiles.models.DocumentsDataModel
import com.backbase.android.flow.uploadfiles.models.RequestedDocumentsModel
import com.backbase.android.flow.uploadfiles.models.UploadDocumentResponse
import com.backbase.android.flow.v2.models.InteractionResponse
import java.io.File

interface UploadFilesUseCase {

    suspend fun requestDocumentList(): InteractionResponse<RequestedDocumentsModel>?
    suspend fun requestDocumentData(groupId: String, internalId: String): InteractionResponse<DocumentsDataModel>?
    suspend fun deleteTempDocument(tempGroupId: String, internalId: String, fileId: String): InteractionResponse<Map<String, Any?>?>?
    suspend fun uploadDocument(tempGroupId: String, internalId: String, file: File): InteractionResponse<UploadDocumentResponse>?
    suspend fun completeTask(): InteractionResponse<Map<String, Any?>?>?
    suspend fun submitDocumentRequests(): InteractionResponse<Map<String, Any?>?>?

}