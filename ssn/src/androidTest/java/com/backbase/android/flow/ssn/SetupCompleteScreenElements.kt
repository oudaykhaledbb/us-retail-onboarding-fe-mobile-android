package com.backbase.android.flow.ssn

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {
    val btnContinueInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnContinue))
    val txtSsnInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtSsn))
    val txtInputSsnInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputSsn))
    val llTopLayoutInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.llTopLayout))
}
