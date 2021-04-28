package com.backbase.android.flow.uploadfiles.usecase

import android.content.Context
import android.webkit.MimeTypeMap
import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.common.utils.readAsset
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.uploadfiles.UploadFilesConfiguration
import com.backbase.android.flow.uploadfiles.models.DocumentsDataModel
import com.backbase.android.flow.uploadfiles.models.RequestedDocumentsModel
import com.backbase.android.flow.uploadfiles.models.UploadDocumentRequest
import com.backbase.android.flow.uploadfiles.models.UploadDocumentResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.reflect.Type
import kotlin.coroutines.suspendCoroutine


private const val JOURNEY_NAME = "sme-onboarding"

class UploadFilesUseCaseImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: UploadFilesConfiguration
) : UploadFilesUseCase {

    override suspend fun requestDocumentList(): RequestedDocumentsModel? {
        return performInteraction<Any, RequestedDocumentsModel>(
            object : TypeToken<RequestedDocumentsModel>() {}.type,
            configuration.requestDocumentAction
        )
    }

    override suspend fun requestDocumentData(): DocumentsDataModel? {
        return performInteraction<Any, DocumentsDataModel>(
            object : TypeToken<DocumentsDataModel>() {}.type,
            configuration.requestDataAction
        )
    }

    override suspend fun deleteTempDocument(
        tempGroupId: String,
        internalId: String,
        fileId: String
    ): Any? {
        return performInteraction<Any, Any?>(
            object : TypeToken<Any?>() {}.type,
            configuration.deleteTempDocumentAction
        )
    }

    override suspend fun uploadDocument(
        tempGroupId: String,
        internalId: String,
        filePath: java.io.File
    ): UploadDocumentResponse? {
        try{
            val response =  performFileInteraction<Any, UploadDocumentResponse>(
                object : TypeToken<UploadDocumentResponse>() {}.type,
                configuration.uploadDocumentAction,
                arrayListOf(filePath),
                UploadDocumentRequest(tempGroupId, internalId)
            )
            return response
        }catch (ex: java.lang.Exception){
            ex.printStackTrace()
            return null
        }

    }

    override suspend fun completeTask(): Any? {
        return performInteraction<Any, Any?>(
            object : TypeToken<Any?>() {}.type,
            configuration.completeTaskAction
        )
    }

    private suspend fun <Request, Response> performInteraction(
        type: Type,
        action: String,
        request: Request? = null
    ): Response? =
        if (configuration.isOffline) performInteractionOffline<Response>(
            type,
            action
        ) else performInteractionOnline<Request, Response>(
            type,
            action,
            request
        )


    private suspend fun <Request, Response> performFileInteraction(
        type: Type,
        action: String,
        filesList: List<java.io.File>,
        request: Request? = null
    ): Response? =
        if (configuration.isOffline) performFileInteractionOffline<Response>(
            type,
            action,
            filesList
        ) else performFileInteractionOnline<Request, Response>(
            type,
            action,
            filesList,
            request
        )


    private fun <Response> performInteractionOffline(
        type: Type,
        action: String
    ): Response? {
        val gson = Gson()
        val rowResponse = readAsset(
            context.assets,
            "backbase/conf/$JOURNEY_NAME/$action.json"
        )
        return gson.fromJson<InteractionResponse<Response?>>(rowResponse, type)?.body
    }

    private suspend fun <Request, Response> performInteractionOnline(
        type: Type,
        action: String,
        request: Request? = null
    ): Response? {
        val rawResponse: com.backbase.android.flow.models.InteractionResponse?
        rawResponse = withContext(Dispatchers.Default) {
            try {
                suspendCoroutine<Any?> { continuation ->
                    flowClient.performInteraction(
                        Action(action, request),
                        InteractionResponseHandler(
                            continuation,
                            action
                        )
                    )
                } as com.backbase.android.flow.models.InteractionResponse?
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@withContext null
            }

        }
        return rawResponse?.body?.autoConvertTo(type)
    }

    private fun <Response> performFileInteractionOffline(
        type: Type,
        action: String,
        filesList: List<java.io.File>
    ): Response? {
        val gson = Gson()
        val rowResponse = readAsset(
            context.assets,
            "backbase/conf/$JOURNEY_NAME/$action.json"
        )
        return gson.fromJson<InteractionResponse<Response?>>(rowResponse, type)?.body
    }

    private suspend fun <Request, Response> performFileInteractionOnline(
        type: Type,
        action: String,
        filesList: List<File>,
        request: Request? = null
    ): Response? {
        val rawResponse: com.backbase.android.flow.models.InteractionResponse?
        rawResponse = withContext(Dispatchers.Default) {
            try {

                val files: ArrayList<ByteArray?> = arrayListOf()
                val fileNames: ArrayList<String?> = arrayListOf()
                val filesContentTypes: ArrayList<String?> = arrayListOf()

                filesList.forEach { fileToUpload ->
                    files.add(fileToUpload.readBytes())
                    fileNames.add(fileToUpload.name)
                    filesContentTypes.add(getMimeType(fileToUpload.absolutePath))
                }

               val response =  suspendCoroutine<Any?> { continuation ->
                    flowClient.performFileInteraction(
                        Action(action, request),
                        files, fileNames, filesContentTypes,
                        InteractionResponseHandler(
                            continuation,
                            action
                        )
                    )
                }
                return@withContext response as com.backbase.android.flow.models.InteractionResponse?
            } catch (ex: Exception) {
                return@withContext null
            }

        }
        return rawResponse?.body?.autoConvertTo(type)
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}


fun <T> Any.autoConvertTo(type: Type): T {
    val gson = Gson()
    return gson.fromJson<T>(gson.toJson(this), type)
}

data class File(
    val fileUrl: String,
    val contentType: String
)