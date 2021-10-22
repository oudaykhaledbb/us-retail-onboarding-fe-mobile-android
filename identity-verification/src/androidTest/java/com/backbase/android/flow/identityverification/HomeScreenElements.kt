package com.backbase.android.flow.identityverification

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.identityverification.R

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object HomeScreenElements {
    val llBiometricValidationsViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.llBiometricValidations))
    val arrowViewInteraction: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.arrow))
    val lblHeaderViewInteraction: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.lblHeader))
    val llScanDocumentViewInteraction: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.llScanDocument))
}