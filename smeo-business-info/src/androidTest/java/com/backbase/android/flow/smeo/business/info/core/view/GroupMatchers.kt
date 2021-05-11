package com.backbase.android.flow.smeo.business.info.core.view

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Matches views that have any one of the given [ids].
 */
fun withOneOfIds(@IdRes vararg ids: Int): Matcher<View> =
    matchingExactlyOneOf(*(Array(ids.size) {
        withId(ids[it])
    }))

private fun matchingExactlyOneOf(vararg viewMatchers: Matcher<View>) = object : TypeSafeMatcher<View>() {

    var overMatchedCount = 0

    override fun matchesSafely(item: View): Boolean {
        var matchedCount = 0
        viewMatchers.forEach {
            if (it.matches(item)) ++matchedCount
        }

        if (matchedCount > 1)
            ++overMatchedCount
        return when (matchedCount) {
            1 -> true
            else -> false
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("exactly one of:\n")
        viewMatchers.forEach { viewMatcher ->
            description.appendText("- ")
            viewMatcher.describeTo(description)
            if (viewMatcher != viewMatchers.last())
                description.appendText("\n")
        }
        if (overMatchedCount > 0)
            description.appendText("\nHowever, $overMatchedCount views matched more than one of these")
    }
}
