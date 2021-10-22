package com.backbase.lookup.business_info

import com.backbase.lookup.business_info.ui.viewmodels.BusinessInfoViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2021-04-10.
 */
internal const val SCOPE_ID = "journey_business"

val businessInfoJourneyModule = module {
    viewModel { BusinessInfoViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}
