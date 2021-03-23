package com.backbase.android.flow.smeo.aboutyou

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {
    val tabTxtInputFirstName: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.txtInputFirstName))
    val tabTxtFirstName: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtFirstName))
    val tabTxtInputLastName: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.txtInputLastName))
    val tabTxtLastName: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtLastName))
    val tabCalendarDateOfBirth: ViewInteraction =
        Espresso.onView(ViewMatchers.withId(R.id.calendarDateOfBirth))
    val tabTxtInputEmail: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtInputEmail))
    val tabTxtEmail: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.txtEmail))
    val tabBtnContinue: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btnContinue))
}
