package com.backbase.android.flow.identityverification

import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.identityverification.core.journey.launchScreen
import com.backbase.android.flow.identityverification.core.journey.on
import com.backbase.android.flow.identityverification.core.koin.KoinTest
import com.backbase.android.flow.identityverification.core.view.checkMatches
import com.backbase.android.flow.identityverification.ui.home.HomeScreen
import org.junit.Test

/**
 * Created by Backbase R&D B.V. on 20/09/2020.
 */
class HomeScreenTest: KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        provideIdentityVerificationConfiguration()
    }

    @Test
    fun testAllViewsAreDisplayed(){
        launchScreen<HomeScreen>()
        on(HomeScreenElements) {
            llScanDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            lblHeaderViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            arrowViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            llBiometricValidationsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }
}