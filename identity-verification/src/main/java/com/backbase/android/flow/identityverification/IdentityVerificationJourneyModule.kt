package com.backbase.android.flow.identityverification

import com.backbase.android.flow.identityverification.ui.scan.DocumentSelectionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2010-09-17.
 */
internal const val SCOPE_ID = "journey_IdentityVerification"

val IdentityVerificationJourneyModule = module {
    single {
        IdentityVerificationViewModel(get(), get())
    }

    viewModel {
        DocumentSelectionViewModel( )
    }

    scope( named(SCOPE_ID)) { }
}
