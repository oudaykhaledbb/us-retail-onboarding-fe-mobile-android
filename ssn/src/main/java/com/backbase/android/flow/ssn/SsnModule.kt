package com.backbase.android.flow.ssn

import com.backbase.android.flow.ssn.ui.SsnViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2021-03-16.
 */
internal const val SCOPE_ID = "journey_ssn"

val ssnJourneyModule = module {
    viewModel { SsnViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}
