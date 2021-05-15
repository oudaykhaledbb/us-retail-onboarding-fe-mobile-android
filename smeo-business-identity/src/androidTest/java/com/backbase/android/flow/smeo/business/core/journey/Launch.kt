package com.backbase.android.flow.smeo.business.core.journey

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.backbase.android.flow.uploadfiles.R

/**
 * Launch a screen fragment with the given [fragmentArgs] and [themeResId].
 *
 * [themeResId] defaults to a material theme.
 */
inline fun <reified S : Fragment> launchScreen(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_Backbase
) = launchFragmentInContainer<S>(fragmentArgs, themeResId, null)
