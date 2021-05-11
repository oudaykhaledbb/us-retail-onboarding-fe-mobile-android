package com.backbase.android.flow.smeo.business.info

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {
    val btnWhenDoYouNeedEinInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnWhenDoYouNeedEin))
    val txtEinInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtEin))
    val txtInputEinInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputEin))
    val btnMoreInfoInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnMoreInfo))
    val txtKnownNameInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtKnownName))
    val txtInputKnownNameInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputKnownName))
    val txtLegalNameInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtLegalName))
    val txtInputLegalNameInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputLegalName))
    val txtBusinessStructureInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtBusinessStructure))
    val btnContinueInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnContinue))
    val txtStateOperatingInInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtStateOperatingIn))
    val txtInputStateOperatingInInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputStateOperatingIn))
    val txtCalendarHelperTextInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtCalendarHelperText))
    val calendarDateEstablishedInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.calendarDateEstablished))
}
