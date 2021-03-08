package com.backbase.android.flow.smeo.walkthrough

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.backbase.android.flow.smeo.walkthrough.core.view.withIndex

/**
 * Setup Complete Screen elements and helper methods for screen verification
 */
object SetupCompleteScreenElements {
    val tabIndicatorViewInteraction: ViewInteraction =
            Espresso.onView(ViewMatchers.withId(R.id.tabIndicator))
    val lblNextViewInteraction: ViewInteraction =
            Espresso.onView(ViewMatchers.withId(R.id.lblNext))
    val viewPagerViewInteraction: ViewInteraction =
            Espresso.onView(ViewMatchers.withId(R.id.viewPager))

    val lblHeaderViewInteraction_page1: ViewInteraction = Espresso.onView(
            withIndex(
                    withText("HeaderTitle2"), 0
            )
    )

    val lblHeaderViewInteraction_page0: ViewInteraction = Espresso.onView(
            withIndex(
                    withText("Header"), 0
            )
    )

    val lblContentViewInteraction_page0: ViewInteraction = Espresso.onView(
            withIndex(
                    withText("Content"), 0
            )
    )

    val lblCaptionViewInteraction_page0: ViewInteraction = Espresso.onView(
            withIndex(
                    withText("Caption"), 0
            )
    )

    val lblHeaderWithIconViewInteraction_page0: ViewInteraction = Espresso.onView(
            withIndex(
                    withText("HeaderWithIcon"), 0
            )
    )

    val lblSupportViewInteraction_page0: ViewInteraction = Espresso.onView(
            withIndex(
                    withText("Support"), 0
            )
    )

    val cbTermsAndConditionsViewInteraction: ViewInteraction = Espresso.onView(ViewMatchers.withId(R.id.cbTermsAndConditions))

}
