package com.backbase.android.flow.productselector

import com.backbase.android.flow.productselector.screen.uicomponent.ProductSelectionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Backbase R&D B.V. on 2020-11-17.
 */
internal const val SCOPE_ID = "journey_product_selector"

val ProductSelectorJourneyModule = module {
    viewModel { ProductSelectionViewModel(get(), get()) }
    scope(named(SCOPE_ID)) { }
}
