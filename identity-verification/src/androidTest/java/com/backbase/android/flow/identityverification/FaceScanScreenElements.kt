package com.backbase.android.flow.identityverification

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.identityverification.R

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object FaceScanScreenElements {
    val tvDocumentTypeViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.tvDocumentType))
    val imgFlashLightViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.imgFlashLight))
    val imgBackViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.imgBack))
    val customScanViewViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.customScanView))
    val customLoadingIndicatorViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.customLoadingIndicator))
    val tvStepsViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.tvSteps))
}
