package com.backbase.android.flow.identityverification.core.view

import android.view.View
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Matches views that have the given [elevation].
 *
 * On API < 21, matches if and only if [elevation] is 0f.
 */
fun withElevation(@Px elevation: Float): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun matchesSafely(item: View): Boolean {
        return ViewCompat.getElevation(item) == elevation
    }

    override fun describeTo(description: Description) {
        description.appendText("with elevation in px: ").appendValue(elevation)
    }
}
