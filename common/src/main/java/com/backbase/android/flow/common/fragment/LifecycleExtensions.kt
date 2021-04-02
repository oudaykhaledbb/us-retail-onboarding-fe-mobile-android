package com.backbase.android.flow.common.fragment

import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.backbase.android.core.utils.BBLogger
import com.backbase.android.flow.common.logging.TAG

/**
 * Extension method for any [LifecycleOwner] for logging its lifecycle events.
 */
fun LifecycleOwner.logLifecycleEvents() {
    lifecycle.addObserver(
        LifecycleEventObserver { lifecycleOwner, event -> BBLogger.debug(lifecycleOwner.TAG, event.toString()) }
    )
}