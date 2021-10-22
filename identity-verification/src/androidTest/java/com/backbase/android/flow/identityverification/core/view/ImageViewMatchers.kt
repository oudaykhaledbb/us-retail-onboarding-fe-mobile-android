package com.backbase.android.flow.identityverification.core.view

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Created by Backbase R&D B.V. on 08/05/2020.
 */
fun withDrawable(drawable: Drawable): Matcher<View> = object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
    override fun matchesSafely(item: ImageView): Boolean {
        val itemDrawable = item.drawable
        return when {
            drawable == itemDrawable -> true
            drawable.javaClass != itemDrawable.javaClass -> false
            else -> when (itemDrawable) {
                is ColorDrawable ->
                    itemDrawable.color == (drawable as ColorDrawable).color
                // TODO MISSING: Compare BitmapDrawables
                else -> false
            }
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable matching ").appendValue(drawable)
    }
}
