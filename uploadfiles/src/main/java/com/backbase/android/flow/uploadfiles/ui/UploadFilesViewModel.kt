package com.backbase.android.flow.uploadfiles.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
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
            val listFileAdvancements: ArrayList<FileAttachments> = arrayListOf()
            documentsList?.documentRequests?.forEach {
                listFileAdvancements.add(FileAttachments(it, useCase.requestDocumentData(it.groupId, it.internalId)?.fileset?.files))
            }
            return@submit listFileAdvancements

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
            return@submit useCase.submitDocumentRequests()
        }
    }

    private fun generateCommonKey(groupID: String, id: String) = "$groupID#$id"

}

data class FileKey(
    val id: String,
    val groupID: String
)