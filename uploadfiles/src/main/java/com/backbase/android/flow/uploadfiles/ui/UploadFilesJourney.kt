package com.backbase.android.flow.uploadfiles.ui

import abhishekti7.unicorn.filepicker.UnicornFilePicker
import abhishekti7.unicorn.filepicker.utils.Constants
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.uploadfiles.R
import com.backbase.android.flow.uploadfiles.models.FileAttachments
import com.backbase.android.flow.uploadfiles.models.UploadDocumentResponse
import com.backbase.android.flow.uploadfiles.showPermissionRequestExplanation
import kotlinx.android.synthetic.main.journey_upload_files.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject
import java.io.File


private const val REQUEST_CODE_CHOOSE = 1
private const val PERMISSION_REQUEST_CODE = 200

class UploadFilesJourney : Fragment(R.layout.journey_upload_files) {

    private lateinit var file: File



    private var adapter: UploadFilesRecyclerViewAdapter? = null
    private val viewModel: UploadFilesViewModel by inject()
    private lateinit var requestWritePermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestReadPermissionLauncher: ActivityResultLauncher<String>

    //TODO to be removed once interaction SDK fixed
    private var uploadResponse: UploadDocumentResponse? = null
    private lateinit var internalId: String
    private lateinit var groupID: String
    private lateinit var fileID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleStateForRequestDocumentListApi(viewModel.apiRequestDocumentList.state)
        handleStateForDeleteDocumentListApi(viewModel.apiDeleteDocument.state)
        handleStateForUploadDocumentListApi(viewModel.apiUploadDocument.state)
        viewModel.requestDocumentList()
        requestWritePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    requestReadPermission()
                }
            }
        requestReadPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    pickFileToUpload(groupID, internalId)
                }
            }
    }

    private fun handleStateForDeleteDocumentListApi(
        apiState: ReceiveChannel<State<FileKey?>>
    ) {
        handleStates(
            apiState,
            { fileKey ->
                adapter?.removeFile(FileKey(this.fileID, groupID))
            },
            null,
            { adapter?.setLoading(true) },
            { adapter?.setLoading(false) }
        )
    }

    private fun handleStateForUploadDocumentListApi(
        apiState: ReceiveChannel<State<UploadDocumentResponse?>>
    ) {
        handleStates(
            apiState,
            { uploadDocumentResponse ->
                if (uploadResponse == null){
                    uploadResponse = UploadDocumentResponse(
                        files = arrayListOf(),
                        id = groupID,
                        name  = ""
                    )
                }
                uploadResponse?.files?.add(com.backbase.android.flow.uploadfiles.models.File(
                    id = System.currentTimeMillis().toString(),
                    mediaType = "Image",
                    name = file.name,
                    tempGroupId = groupID
                ))
                adapter?.addFile(uploadResponse)
            },
            null,
            { adapter?.setLoading(true) },
            { adapter?.setLoading(false) }
        )
    }

    private fun handleStateForRequestDocumentListApi(
        apiState: ReceiveChannel<State<List<FileAttachments>?>>
    ) {
        handleStates(
            apiState,
            { filesToUpload ->
                filesToUpload?.let { populateDocumentList(it) }
            },
            null,
            { adapter?.setLoading(true) },
            { adapter?.setLoading(false) }
        )
    }

    private fun populateDocumentList(filesToUpload: List<FileAttachments>) {
        adapter = UploadFilesRecyclerViewAdapter(filesToUpload)
        rvUploadFiles.layoutManager = LinearLayoutManager(requireContext())
        rvUploadFiles.adapter = adapter
        adapter?.onContinue = {
            onContinue()
        }
        adapter?.onDeleteFileListener = { groupID, id, internalId ->
            this.fileID = id
            viewModel.deleteDocument(groupID, id, internalId)
        }
        adapter?.onUploadButtonClickListener = {
            groupID = it.documentRequest.groupId
            internalId = it.documentRequest.internalId

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ){
                pickFileToUpload(groupID, internalId)
            }else{
                requestWritePermission()
            }
        }

    }

    private fun onContinue() {
        viewModel.completeTask()
    }


    private fun requestReadPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)-> {
                requireContext().showPermissionRequestExplanation(
                    "Permission needed",
                    "Read External storage is required"
                ) { requestWritePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE) }
            }
            else -> {
                // Everything is fine you can simply request the permission
                requestWritePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun requestWritePermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                requireContext().showPermissionRequestExplanation(
                    "Permission needed",
                    "Read External storage is required"
                ) { requestWritePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            }
            else -> {
                // Everything is fine you can simply request the permission
                requestWritePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 9999 && resultCode == RESULT_OK) {
            val files = data!!.getStringArrayListExtra("filePaths")
            for (file in files!!) {
                val file1 = "/storage/emulated/0/DCIM/Camera/SAMPLE_pdf.pdf"
                this.file = File(file1)
                viewModel.uploadDocument(groupID, internalId, File(file1))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun pickFileToUpload(groupId: String, internalId: String) {
        this.groupID = groupId
        this.internalId = internalId
        UnicornFilePicker.from(this)
            .addConfigBuilder()
            .selectMultipleFiles(false)
            .showOnlyDirectory(false)
            .setRootDirectory(
                Environment.getExternalStorageDirectory().absolutePath
            )
            .showHiddenFiles(true)
            .setFilters(arrayOf("pdf", "png", "jpg", "jpeg"))
            .addItemDivider(true)
            .theme(R.style.UnicornFilePicker_Dracula)
            .build()
            .forResult(Constants.REQ_UNICORN_FILE)
    }

}