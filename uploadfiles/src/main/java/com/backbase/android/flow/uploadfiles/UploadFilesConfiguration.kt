package com.backbase.android.flow.uploadfiles

class UploadFilesConfiguration private constructor(
    val isOffline: Boolean,
    val requestDocumentAction: String,
    val requestDataAction: String,
    val uploadDocumentAction: String,
    val deleteTempDocumentAction: String,
    val submitDocumentAction: String,
    val completeTaskAction: String
) {
    class Builder {

        @set: JvmSynthetic
        var isOffline: Boolean = false

        @set:JvmSynthetic
        lateinit var submitDocumentAction: String

        @set:JvmSynthetic
        lateinit var uploadDocumentAction: String


        @set:JvmSynthetic
        lateinit var deleteTempDocumentAction: String

        @set:JvmSynthetic
        lateinit var requestDocumentAction: String

        @set:JvmSynthetic
        lateinit var requestDataAction: String

        @set:JvmSynthetic
        lateinit var completeTaskAction: String

        fun build() = UploadFilesConfiguration(
            isOffline,
            requestDocumentAction,
            requestDataAction,
            uploadDocumentAction,
            deleteTempDocumentAction,
            submitDocumentAction,
            completeTaskAction
        )

    }
}

/**
 * DSL function to create a [WalkthroughConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun uploadFilesConfiguration(initializer: UploadFilesConfiguration.Builder.() -> Unit): UploadFilesConfiguration =
    UploadFilesConfiguration.Builder().apply(initializer).build()