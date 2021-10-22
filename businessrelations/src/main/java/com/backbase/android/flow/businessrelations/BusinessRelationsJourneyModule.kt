package com.backbase.android.flow.businessrelations

import com.backbase.android.flow.businessrelations.ui.viewmodel.AddBusinessOwnerViewModel
import com.backbase.android.flow.businessrelations.ui.viewmodel.RoleSelectionViewModel
import com.backbase.android.flow.businessrelations.ui.viewmodel.SelectControlPersonViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2020-08-01.
 */
internal const val SCOPE_ID = "journey_business_relations"

val BusinessRelationsJourneyModule = module {
    viewModel { RoleSelectionViewModel(get()) }
    viewModel { AddBusinessOwnerViewModel(get()) }
    viewModel { SelectControlPersonViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}
