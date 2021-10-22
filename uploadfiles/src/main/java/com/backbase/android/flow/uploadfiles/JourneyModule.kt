package com.backbase.android.flow.uploadfiles

import com.backbase.android.flow.uploadfiles.ui.UploadFilesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2021-04-10.
 */
internal const val SCOPE_ID = "uploadFiles_document-request"

val uploadJourneyModule = module {
    viewModel { UploadFilesViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}
