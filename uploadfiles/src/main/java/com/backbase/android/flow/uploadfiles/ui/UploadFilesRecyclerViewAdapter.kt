package com.backbase.android.flow.uploadfiles.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.uploadfiles.R
import com.backbase.android.flow.uploadfiles.models.File
import com.backbase.android.flow.uploadfiles.models.FileAttachments
import com.backbase.android.flow.uploadfiles.models.UploadDocumentResponse
import kotlinx.android.synthetic.main.row_documents.view.*
import kotlinx.android.synthetic.main.row_upload_files.view.*
import kotlinx.android.synthetic.main.row_upload_later.view.*

class UploadFilesRecyclerViewAdapter(private val filesToUpload: List<FileAttachments>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var onUploadButtonClickListener: ((fileAttachment: FileAttachments) -> Unit)? = null
    internal var onDeleteFileListener: ((groupID: String, id: String, internalID: String) -> Unit)? =
        null
    internal var onContinue: (() -> Unit)? = null
    private var isLoading: Boolean = false
    private val loadableButtons = arrayListOf<BackbaseButton>()
    private val viewsToDisableOnLoading = arrayListOf<View>()
    private var isUploadDocumentLaterEnabled = false

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        loadableButtons.forEach {
            try {
                it.loading = isLoading
            } catch (ex: Exception) {/*DoNothing*/
            }
        }
        viewsToDisableOnLoading.forEach {
            try {
                it.isEnabled = !isLoading
            } catch (ex: Exception) {/*DoNothing*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return if (viewType == R.layout.row_upload_files) {
            UploadFilesViewHolder(view)
        } else {
            UploadFilesLaterViewHolder(view)
        }
    }

    override fun getItemCount() = filesToUpload.size + 1

    override fun getItemViewType(position: Int) =
        if (position < filesToUpload.size) R.layout.row_upload_files
        else R.layout.row_upload_later


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UploadFilesViewHolder) {
            holder.bind(filesToUpload[position])
        } else {
            (holder as UploadFilesLaterViewHolder).bind()
        }
    }

    fun removeFile(fileKey: FileKey?) {
        val fileToUpload = filesToUpload.find { it.documentRequest.groupId == fileKey?.groupID }
        fileToUpload?.files?.apply {
            val fileToRemove = fileToUpload?.files?.find { it.id == fileKey?.id }
            remove(fileToRemove)
            notifyItemChanged(filesToUpload.indexOf(fileToUpload))
        }
    }

    fun addFile(uploadDocumentResponse: UploadDocumentResponse?) {
        val fileToUpload =
            filesToUpload.find { it.documentRequest.groupId == uploadDocumentResponse?.files?.get(0)?.tempGroupId }
        if (fileToUpload?.files == null) {
            fileToUpload?.files = arrayListOf()
        }
        fileToUpload?.files?.apply {
            clear()
            uploadDocumentResponse?.files?.let { addAll(it) }
            notifyItemChanged(filesToUpload.indexOf(fileToUpload))
        }
    }

    inner class UploadFilesLaterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.btnContinue.setOnClickListener {
                onContinue?.invoke()
            }
            itemView.cbUploadDocumentLater.setOnCheckedChangeListener { _, isChecked ->
                isUploadDocumentLaterEnabled = isChecked
                notifyDataSetChanged()
            }
        }
    }

    inner class UploadFilesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("UseCompatLoadingForColorStateLists")
        fun bind(fileAttachments: FileAttachments) {
            itemView.lblTitle.text = fileAttachments.documentRequest.filesetName
            itemView.lblDescription.text = fileAttachments.documentRequest.initialNote
            loadableButtons.add(itemView.btnUpload)
            itemView.btnUpload.loading = isLoading
            itemView.btnUpload.setOnClickListener {
                onUploadButtonClickListener?.invoke(
                    fileAttachments
                )
            }
            itemView.llUploadedDocumentsList.removeAllViews()
            fileAttachments.files?.let {
                bindAttachments(
                    fileAttachments.documentRequest.internalId,
                    it
                )
            }
            itemView.btnUpload.isEnabled = !isUploadDocumentLaterEnabled //&& fileAttachments.files?.isNotEmpty() != false

        }

        private fun bindAttachments(
            internalId: String,
            files: List<File>
        ) {
            itemView.llUploadedDocuments.visibility =
                if (files.isEmpty()) View.GONE else View.VISIBLE

            files.forEach { file ->
                val view = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.row_documents, null, false)
                view.lblFileName.text = file.name
                view.imgFileIcon.setImageResource(
                    if (file.mediaType.toLowerCase().contains("pdf"))
                        R.drawable.ic_file_pdf else R.drawable.ic_file_img
                )
                viewsToDisableOnLoading.add(itemView.btnRemove)
                view.btnRemove.isEnabled = !isLoading
                view.btnRemove.setOnClickListener {
                    onDeleteFileListener?.invoke(
                        file.tempGroupId,
                        file.id,
                        internalId
                    )
                }
                itemView.llUploadedDocumentsList.addView(view)
            }
        }
    }

}