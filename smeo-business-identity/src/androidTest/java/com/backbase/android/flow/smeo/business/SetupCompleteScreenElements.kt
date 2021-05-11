package com.backbase.android.flow.smeo.business

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {
    val txtCompanyWebsiteInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtCompanyWebsite))
    val txtBusinessDescriptionInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtBusinessDescription))
    val txtInputBusinessDescriptionInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputBusinessDescription))
    val txtIndustryInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtIndustry))
    val txtInputIndustryInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputIndustry))
    val btnContinueInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnContinue))
}
