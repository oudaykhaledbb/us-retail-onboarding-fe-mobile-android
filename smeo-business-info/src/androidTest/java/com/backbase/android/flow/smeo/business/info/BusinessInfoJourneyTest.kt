package com.backbase.android.flow.smeo.business.info

import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.smeo.business.info.core.journey.launchScreen
import com.backbase.android.flow.smeo.business.info.core.journey.on
import com.backbase.android.flow.smeo.business.info.core.koin.KoinTest
import com.backbase.android.flow.smeo.business.info.core.view.checkMatches
import com.backbase.android.flow.smeo.business.info.ui.screens.BusinessInfoJourney
import org.junit.Test

class BusinessInfoJourneyTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideBusinessInfoConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<BusinessInfoJourney>()
        on(SetupCompleteScreenElements) {
            btnWhenDoYouNeedEinInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtEinInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputEinInteraction.checkMatches(ViewMatchers.isDisplayed())
            btnMoreInfoInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtKnownNameInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputKnownNameInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtLegalNameInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputLegalNameInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtBusinessStructureInteraction.checkMatches(ViewMatchers.isDisplayed())
            btnContinueInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtStateOperatingInInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputStateOperatingInInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtCalendarHelperTextInteraction.checkMatches(ViewMatchers.isDisplayed())
            calendarDateEstablishedInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

}