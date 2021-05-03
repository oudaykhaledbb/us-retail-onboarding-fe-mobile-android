package com.backbase.android.flow.uploadfiles.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.uploadfiles.models.File
import com.backbase.android.flow.uploadfiles.models.FileAttachments
import com.backbase.android.flow.uploadfiles.models.UploadDocumentResponse
import com.backbase.android.flow.uploadfiles.usecase.UploadFilesUseCase

class UploadFilesViewModel(private val useCase: UploadFilesUseCase) : ViewModel() {

    internal val apiRequestDocumentList = ApiPublisher<List<FileAttachments>?>(this.viewModelScope)
    internal val apiUploadDocument = ApiPublisher<UploadDocumentResponse?>(this.viewModelScope)
    internal val apiDeleteDocument = ApiPublisher<FileKey?>(this.viewModelScope)
    internal val apiCompleteTask = ApiPublisher<Any?>(this.viewModelScope)

    fun requestDocumentList() {
        apiRequestDocumentList.submit("requestDocumentList()") {
            val documentsList = useCase.requestDocumentList()
            val documentData = useCase.requestDocumentData()
            val searchableDocumentData = HashMap<String, ArrayList<File>>()
            documentData?.fileset?.files?.forEach {
                val filesList = searchableDocumentData[generateCommonKey(it.tempGroupId, it.id)]
                    ?: arrayListOf()
                filesList.add(it)
                searchableDocumentData[generateCommonKey(it.tempGroupId, it.id)] = filesList
            }
            return@submit documentsList?.documentRequests?.map {
                FileAttachments(
                    it, searchableDocumentData?.get(generateCommonKey(it.groupId, it.internalId))
                )
            }?.toList()
        }
    }

    fun uploadDocument(tempGroupId: String, internalId: String, file: java.io.File) {
        apiUploadDocument.submit("uploadDocument()") {
            return@submit useCase.uploadDocument(tempGroupId, internalId, file)
        }
    }

    fun deleteDocument(tempGroupId: String, internalId: String, fileId: String) {
        apiDeleteDocument.submit("deleteDocument()") {
            useCase.deleteTempDocument(tempGroupId, internalId, fileId)
            return@submit FileKey(internalId, tempGroupId)
        }
    }

    fun completeTask() {
        apiCompleteTask.submit("completeTask()") {
            useCase.submitDocumentRequests()
            return@submit useCase.completeTask()
        }
    }

    private fun generateCommonKey(groupID: String, id: String) = "$groupID#$id"

    private suspend fun requestDocumentListInternal(): List<FileAttachments>? {
        val documentsList = useCase.requestDocumentList()
        val documentData = useCase.requestDocumentData()
        val searchableDocumentData = HashMap<String, ArrayList<File>>()
        documentData?.fileset?.files?.forEach {
            val filesList = searchableDocumentData[generateCommonKey(it.tempGroupId, it.id)]
                ?: arrayListOf()
            filesList.add(it)
            searchableDocumentData[generateCommonKey(it.tempGroupId, it.id)] = filesList
        }
        return documentsList?.documentRequests?.map {
            FileAttachments(
                it, searchableDocumentData?.get(generateCommonKey(it.groupId, it.internalId))
            )
        }?.toList()
    }
}

data class FileKey(
    val id: String,
    val groupID: String
)