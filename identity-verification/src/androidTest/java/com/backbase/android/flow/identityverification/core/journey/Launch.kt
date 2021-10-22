package com.backbase.android.flow.identityverification.core.journey

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.fragment.app.testing.launchFragmentInContainer
import com.backbase.android.flow.identityverification.R

/**
 * Launch a Journey fragment with the given [fragmentArgs] and [themeResId].
 *
 * [themeResId] defaults to a material theme.
 */
inline fun <reified J : Fragment> launchJourney(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_Backbase
) = launchFragmentInContainer<J>(fragmentArgs, themeResId, null).apply {
    // Ensure Journey is registered as navigation fragment: https://stackoverflow.com/a/60142746
    onFragment { journey ->
        journey.parentFragmentManager.commitNow {
            setPrimaryNavigationFragment(journey)
        }
    }
}

/**
 * Launch a screen fragment with the given [fragmentArgs] and [themeResId].
 *
 * [themeResId] defaults to a material theme.
 */
inline fun <reified S : Fragment> launchScreen(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_Backbase
) = launchFragmentInContainer<S>(fragmentArgs, themeResId, null)
