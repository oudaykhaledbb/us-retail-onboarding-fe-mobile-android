package com.backbase.android.flow.smeo.business

import com.backbase.android.flow.smeo.business.ui.viewmodels.BusinessIdentityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2021-04-10.
 */
internal const val SCOPE_ID = "journey_business_identity"

val businessIdentityJourneyModule = module {
    viewModel { BusinessIdentityViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}
