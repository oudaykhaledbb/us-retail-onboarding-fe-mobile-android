package com.backbase.android.flow.smeo.walkthrough.core.view

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.endsWith
import org.hamcrest.core.IsAnything

fun inSnackbar(snackbarMatcher: Matcher<View> = IsAnything()) = ViewMatchers.isDescendantOfA(
    allOf(withClassName(endsWith("Snackbar\$SnackbarLayout")), snackbarMatcher)
)
