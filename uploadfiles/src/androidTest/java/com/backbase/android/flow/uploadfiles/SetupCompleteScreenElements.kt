package com.backbase.android.flow.uploadfiles

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {
    val rvUploadFiles: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.rvUploadFiles))
}
