package com.backbase.android.flow.smeo.business

import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.smeo.business.core.journey.launchScreen
import com.backbase.android.flow.smeo.business.core.journey.on
import com.backbase.android.flow.smeo.business.core.koin.KoinTest
import com.backbase.android.flow.smeo.business.core.view.checkMatches
import com.backbase.android.flow.smeo.business.ui.screens.BusinessIdentityJourney
import org.junit.Test

class BusinessIdentityJourneyTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideBusinessIdentityConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<BusinessIdentityJourney>()
        on(SetupCompleteScreenElements) {
            txtCompanyWebsiteInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtBusinessDescriptionInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputBusinessDescriptionInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtIndustryInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputIndustryInteraction.checkMatches(ViewMatchers.isDisplayed())
            btnContinueInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

}