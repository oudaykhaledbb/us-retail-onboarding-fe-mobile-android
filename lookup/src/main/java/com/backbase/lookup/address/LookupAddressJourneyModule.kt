package com.backbase.lookup.address

import com.backbase.lookup.address.ui.AddressViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2010-06-30.
 */
internal const val SCOPE_ID = "journey_address"

val addressJourneyModule = module {
    viewModel { AddressViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}