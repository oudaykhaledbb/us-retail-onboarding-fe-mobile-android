package com.backbase.android.flow.smeo.walkthrough

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.smeo.walkthrough.ui.WalkthroughJourney
import com.backbase.android.flow.smeo.walkthrough.core.journey.launchScreen
import com.backbase.android.flow.walkthrough.core.journey.on
import com.backbase.android.flow.smeo.walkthrough.core.koin.KoinTest
import com.backbase.android.flow.smeo.walkthrough.core.view.checkMatches
import kotlinx.coroutines.delay
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class WalkthroughScreenTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideWalkthroughConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<WalkthroughJourney>()
        on(SetupCompleteScreenElements) {
            lblHeaderWithIconViewInteraction_page0.checkMatches(ViewMatchers.isDisplayed())
            lblHeaderViewInteraction_page0.checkMatches(ViewMatchers.isDisplayed())
            lblContentViewInteraction_page0.checkMatches(ViewMatchers.isDisplayed())
            lblSupportViewInteraction_page0.checkMatches(ViewMatchers.isDisplayed())
            lblCaptionViewInteraction_page0.checkMatches(ViewMatchers.isDisplayed())
            viewPagerViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            lblNextViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tabIndicatorViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testPageAreChangeSuccessfully(){
        launchScreen<WalkthroughJourney>()
        on(SetupCompleteScreenElements) {
            lblNextViewInteraction.perform(click())
            lblHeaderViewInteraction_page1.checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testNextButtonDisabledUntilCheckBoxChecked(){
        launchScreen<WalkthroughJourney>()
        on(SetupCompleteScreenElements) {
            lblNextViewInteraction.perform(click())
            lblNextViewInteraction.checkMatches(not(ViewMatchers.isEnabled()))
        }
    }

}