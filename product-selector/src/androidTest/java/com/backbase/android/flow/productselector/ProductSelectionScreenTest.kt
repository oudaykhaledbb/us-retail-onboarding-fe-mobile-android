package com.backbase.android.flow.productselector

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.productselector.core.journey.launchScreen
import com.backbase.android.flow.productselector.core.journey.on
import com.backbase.android.flow.productselector.core.koin.KoinTest
import com.backbase.android.flow.productselector.core.koin.provide
import com.backbase.android.flow.productselector.core.view.checkMatches
import com.backbase.android.flow.productselector.screen.ProductSelectionScreen
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class ProductSelectionScreenTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideProductSelectorConfigurationModule()
        provide<ProductSelectorUseCase> {
            FakeProductSelectorUseCaseImpl()
        }

        var dbsModule = module {
            factory { NetworkDBSDataProvider(get()) }
        }
        loadKoinModules(dbsModule)
        loadKoinModules(ProductSelectorJourneyModule)
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<ProductSelectionScreen>()
        on(SetupCompleteScreenElements) {
            rvProductViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            indicatorViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            btnHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testElementsAreOrderedProperly(){
        launchScreen<ProductSelectionScreen>()
        on(SetupCompleteScreenElements) {
            
            lblHeaderViewInteraction_page0.checkMatches(ViewMatchers.withText("VISA Platinum 1"))
            lblHeaderViewInteraction_page1.checkMatches(ViewMatchers.withText("VISA Platinum 2"))
            lblHeaderViewInteraction_page2.checkMatches(ViewMatchers.withText("VISA Platinum 3"))
            lblHeaderViewInteraction_page3.checkMatches(ViewMatchers.withText("VISA Platinum 4"))
        }
    }

    @Test
    fun testHideTickWhenProductNotSelected(){
        launchScreen<ProductSelectionScreen>()
        on(SetupCompleteScreenElements) {
            
            imgTickViewInteraction_page0.check(ViewAssertions.matches(withEffectiveVisibility(
                ViewMatchers.Visibility.GONE)))
        }
    }

}