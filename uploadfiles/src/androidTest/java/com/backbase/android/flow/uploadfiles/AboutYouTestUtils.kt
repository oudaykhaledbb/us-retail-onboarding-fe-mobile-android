package com.backbase.android.flow.uploadfiles

import android.os.Looper
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


private val backbaseModule = module {
    factory {
        //BBIdentityAuthClient constructor creates a Handler so calling thread needs to prepare looper.
        if (Looper.myLooper() == null) Looper.prepare()
    }
}

private val aboutYouConfigurationModule = module {

    factory {
        uploadFilesConfiguration{
            isOffline = false
            supportedFiles = arrayListOf("pdf", "png", "jpg", "jpeg")
            requestDocumentAction = "load-document-requests"
            requestDataAction = "load-document-request"
            uploadDocumentAction = "upload-document"
            deleteTempDocumentAction = "delete-temp-document"
            submitDocumentAction = "submit-document-requests"
            completeTaskAction = "complete-task"
        }
    }
}

/**
 * Provide Backbase specific dependencies for tests, which are otherwise provided from application level.
 */
fun provideBackbaseDependencies() = loadKoinModules(backbaseModule)

/**
 * Provide Backbase specific dependencies for tests, which are otherwise provided from application level.
 */
fun provideAboutYouConfiguration() = loadKoinModules(aboutYouConfigurationModule)
