package com.backbase.android.flow.productselector.core.fragment

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario

/**
 * Get a value using the fragment's Context.
 */
inline fun <T : Any> FragmentScenario<*>.getFromContext(crossinline block: (Context) -> T): T {
    var t: T? = null
    onFragment { fragment ->
        t = block(fragment.requireContext())
    }
    val safeT = t
    checkNotNull(safeT)
    return safeT
}
