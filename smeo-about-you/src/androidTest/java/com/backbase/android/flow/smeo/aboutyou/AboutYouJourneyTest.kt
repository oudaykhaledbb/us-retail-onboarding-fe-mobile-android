package com.backbase.android.flow.smeo.aboutyou

import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.smeo.aboutyou.core.journey.launchScreen
import com.backbase.android.flow.smeo.aboutyou.core.journey.on
import com.backbase.android.flow.smeo.aboutyou.core.koin.KoinTest
import com.backbase.android.flow.smeo.aboutyou.core.view.checkMatches
import com.backbase.android.flow.smeo.aboutyou.ui.AboutYouJourney
import org.junit.Test

class AboutYouJourneyTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideAboutYouConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<AboutYouJourney>()
        on(SetupCompleteScreenElements) {
            tabTxtInputFirstName.checkMatches(ViewMatchers.isDisplayed())
            tabTxtFirstName.checkMatches(ViewMatchers.isDisplayed())
            tabTxtInputLastName.checkMatches(ViewMatchers.isDisplayed())
            tabTxtLastName.checkMatches(ViewMatchers.isDisplayed())
            tabCalendarDateOfBirth.checkMatches(ViewMatchers.isDisplayed())
            tabTxtInputEmail.checkMatches(ViewMatchers.isDisplayed())
            tabTxtEmail.checkMatches(ViewMatchers.isDisplayed())
            tabBtnContinue.checkMatches(ViewMatchers.isDisplayed())
        }
    }

}