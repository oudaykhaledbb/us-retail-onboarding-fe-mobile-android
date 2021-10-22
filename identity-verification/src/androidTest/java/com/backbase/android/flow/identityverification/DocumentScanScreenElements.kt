package com.backbase.android.flow.identityverification

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object DocumentScanScreenElements {
    val tvDocumentTypeViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.tvDocumentType))
    val imgFlashLightViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.imgFlashLight))
    val imgBackViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.imgBack))
    val btnDismissHelpViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnDismissHelp))
    val btnSkipNfcViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnSkipNfc))
    val customConfirmationViewViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.customConfirmationView))
    val customScanViewViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.customScanView))
    val clCaptureDocumentViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.clCaptureDocument))
    val customAnimationViewViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.customAnimationView))
    val tvHelpViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.tvHelp))
    val customLoadingIndicatorViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.customLoadingIndicator))
    val btnRetakeViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnRetake))
    val btnConfirmViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnConfirm))
    val btnCaptureViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnCapture))
    val btnFallbackViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnFallback))
    val tvStepsViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.tvSteps))
}
