package com.backbase.android.flow.ssn

import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.ssn.core.journey.launchScreen
import com.backbase.android.flow.ssn.core.journey.on
import com.backbase.android.flow.ssn.core.koin.KoinTest
import com.backbase.android.flow.ssn.core.view.checkMatches
import com.backbase.android.flow.ssn.ui.SsnJourney
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class SsnScreenTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideWalkthroughConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<SsnJourney>()
        on(SetupCompleteScreenElements) {
            llTopLayoutInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtInputSsnInteraction.checkMatches(ViewMatchers.isDisplayed())
            txtSsnInteraction.checkMatches(ViewMatchers.isDisplayed())
            btnContinueInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testDisableBtnContinueWhenSsnLess9Chars(){
        launchScreen<SsnJourney>()
        on(SetupCompleteScreenElements) {
            txtInputSsnInteraction.perform(typeText("123"))
            btnContinueInteraction.checkMatches(not(ViewMatchers.isEnabled()))
        }
    }

    @Test
    fun testEnableBtnContinueWhenSsnGreater9Chars(){
        launchScreen<SsnJourney>()
        on(SetupCompleteScreenElements) {
            txtInputSsnInteraction.perform(typeText("123123123"))
            btnContinueInteraction.checkMatches(ViewMatchers.isEnabled())
        }
    }

}