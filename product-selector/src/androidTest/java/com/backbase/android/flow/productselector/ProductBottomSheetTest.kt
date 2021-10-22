package com.backbase.android.flow.productselector

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.productselector.core.journey.launchScreen
import com.backbase.android.flow.productselector.core.journey.on
import com.backbase.android.flow.productselector.core.koin.KoinTest
import com.backbase.android.flow.productselector.core.koin.provide
import com.backbase.android.flow.productselector.core.view.checkMatches
import com.backbase.android.flow.productselector.core.view.withIndex
import com.backbase.android.flow.productselector.screen.ProductSelectionScreen
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class ProductBottomSheetTest : KoinTest() {

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
    fun testShowBottomSheetWhenCardIsClicked(){
        launchScreen<ProductSelectionScreen>()
        on(SetupCompleteScreenElements) {
            onView(withIndex(withId(R.id.imgProduct), 0)).perform(ViewActions.click())
            onView(withId(R.id.btnContinue)).checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testBottomSheetTitleShowValidProductDataForCard1(){
        launchScreen<ProductSelectionScreen>()
        on(SetupCompleteScreenElements) {
            onView(withIndex(withId(R.id.imgProduct), 0)).perform(ViewActions.click())
            onView(withId(R.id.lblHeader)).checkMatches(ViewMatchers.withText("VISA Platinum 1"))
        }
    }

    @Test
    fun testBottomSheetDescriptionShowValidProductDataForCard1(){
        launchScreen<ProductSelectionScreen>()
        on(SetupCompleteScreenElements) {
            onView(withIndex(withId(R.id.imgProduct), 0)).perform(ViewActions.click())
            onView(withId(R.id.lblProductDescription)).checkMatches(ViewMatchers.withText("Description 1"))
        }
    }

}