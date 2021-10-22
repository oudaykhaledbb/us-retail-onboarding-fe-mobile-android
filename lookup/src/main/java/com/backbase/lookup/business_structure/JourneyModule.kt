package com.backbase.lookup.business_structure

import com.backbase.lookup.ScreensMonitor
import com.backbase.lookup.business_structure.ui.viewmodels.BusinessStructureViewModel
import com.backbase.lookup.business_structure.ui.viewmodels.CompanyLookupViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2021-06-14.
 */
internal const val SCOPE_ID = "journey_business_structure"

val businessJourneyModule = module {
    viewModel { BusinessStructureViewModel(get()) }
    viewModel { CompanyLookupViewModel(get()) }
    single { ScreensMonitor() }
    scope(named(SCOPE_ID)) { }
}
