package com.backbase.android.flow.smeo.aboutyou


import com.backbase.android.flow.smeo.aboutyou.ui.AboutYouViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2021-03-22.
 */
internal const val SCOPE_ID = "journey_sme_about_you"

val aboutYouJourneyModule = module {
    viewModel { AboutYouViewModel(get()) }
    scope(named(SCOPE_ID)) { }
}
