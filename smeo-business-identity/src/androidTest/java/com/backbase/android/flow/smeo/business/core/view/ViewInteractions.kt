package com.backbase.android.flow.smeo.business.core.view

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * A convenience for the frequently-called `check(matches(viewMatcher))` syntax.
 */
fun ViewInteraction.checkMatches(viewMatcher: Matcher<in View>): ViewInteraction = check(matches(viewMatcher))

fun withIndex(
    matcher: Matcher<View?>,
    index: Int
): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {
        var currentIndex = 0
        override fun describeTo(description: Description) {
            description.appendText("with index: ")
            description.appendValue(index)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            return matcher.matches(view) && currentIndex++ == index
        }
    }
}