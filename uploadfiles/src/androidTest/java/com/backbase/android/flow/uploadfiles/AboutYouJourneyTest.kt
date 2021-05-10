package com.backbase.android.flow.uploadfiles

import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.uploadfiles.core.journey.launchScreen
import com.backbase.android.flow.uploadfiles.core.journey.on
import com.backbase.android.flow.uploadfiles.core.koin.KoinTest
import com.backbase.android.flow.uploadfiles.core.view.checkMatches
import com.backbase.android.flow.uploadfiles.ui.UploadFilesJourney
import org.junit.Test

class AboutYouJourneyTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideAboutYouConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed() {
        launchScreen<UploadFilesJourney>()
        on(SetupCompleteScreenElements) {
            rvUploadFiles.checkMatches(ViewMatchers.isDisplayed())
        }
    }

}