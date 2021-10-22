package com.backbase.android.flow.productselector

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.backbase.android.flow.productselector.core.view.withIndex

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {

    val btnHelpViewInteraction: ViewInteraction =
        Espresso.onView((ViewMatchers.withId(R.id.btnHelp)))
    val indicatorViewInteraction: ViewInteraction =
        Espresso.onView((ViewMatchers.withId(R.id.indicator)))
    val rvProductViewInteraction: ViewInteraction =
        Espresso.onView((ViewMatchers.withId(R.id.rvProduct)))

    val imgTickViewInteraction_page0: ViewInteraction = Espresso.onView(
        withIndex(withId(
            R.id.imgTick
        ), 0)
    )

    val imgTickViewInteraction_page1: ViewInteraction = Espresso.onView(
        withIndex(withId(
            R.id.imgTick
        ), 1)
    )

    val lblHeaderViewInteraction_page0: ViewInteraction = Espresso.onView(
        withIndex(withId(
            R.id.lblHeader
        ), 0)
    )

    val lblHeaderViewInteraction_page1: ViewInteraction = Espresso.onView(
        withIndex(withId(
            R.id.lblHeader
        ), 1)
    )

    val lblHeaderViewInteraction_page2: ViewInteraction = Espresso.onView(
        withIndex(withId(
            R.id.lblHeader
        ), 2)
    )

    val lblHeaderViewInteraction_page3: ViewInteraction = Espresso.onView(
        withIndex(withId(
            R.id.lblHeader
        ), 3)
    )

}
