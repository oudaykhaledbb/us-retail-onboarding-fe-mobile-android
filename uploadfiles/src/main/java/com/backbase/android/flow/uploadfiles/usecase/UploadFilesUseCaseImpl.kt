package com.backbase.android.flow.uploadfiles.usecase

import android.content.Context
import android.webkit.MimeTypeMap
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.uploadfiles.UploadFilesConfiguration
import com.backbase.android.flow.uploadfiles.models.*
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


private const val JOURNEY_NAME = "sme-onboarding-upload-files"

class UploadFilesUseCaseImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: UploadFilesConfiguration
) : UploadFilesUseCase {

    override suspend fun requestDocumentList() =
        flowClient.performInteraction<RequestedDocumentsModel>(
            Action(
                configuration.requestDocumentAction, null
            ), RequestedDocumentsModel::class.java
        )

    override suspend fun requestDocumentData(
        groupId: String,
        internalId: String
    ) = flowClient.performInteraction<DocumentsDataModel>(
        Action(
            configuration.requestDataAction, LoadDocumentModel(internalId, groupId)
        ),
        object : TypeToken<DocumentsDataModel>() {}.type
    )

    override suspend fun deleteTempDocument(
        tempGroupId: String,
        internalId: String,
        fileId: String
    ) = flowClient.performInteraction<Map<String, Any?>?>(
        Action(
            configuration.deleteTempDocumentAction, FileToDeleteModel(
                tempGroupId = tempGroupId,
                internalId = internalId,
                fileId = fileId
            )
        ),
        object : TypeToken<Map<String, Any?>?>() {}.type
    )

    override suspend fun uploadDocument(
        tempGroupId: String,
        internalId: String,
        filePath: File
    ): InteractionResponse<UploadDocumentResponse>? {
        val uploadDocumentRequest = UploadDocumentRequest(tempGroupId, internalId)
        val responseType: Type =
            object : TypeToken<UploadDocumentResponse>() {}.type

        return flowClient.performFileInteraction(
            Action(configuration.uploadDocumentAction, uploadDocumentRequest),
            arrayListOf(filePath.readBytes()),
            arrayListOf(filePath.name),
            arrayListOf(getMimeType(filePath.absolutePath)),
            responseType
        )
    }

    override suspend fun completeTask() =
        flowClient.performInteraction<Map<String, Any?>?>(
            Action(configuration.completeTaskAction, null),
            object : TypeToken<Map<String, Any?>?>() {}.type
        )

    override suspend fun submitDocumentRequests() =
        flowClient.performInteraction<Map<String, Any?>?>(
            Action(configuration.submitDocumentAction, null),
            object : TypeToken<Map<String, Any?>?>() {}.type
        )

    private fun getMimeType(url: String?): String {
        var type: String = ""
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
        }
        return type
    }
}