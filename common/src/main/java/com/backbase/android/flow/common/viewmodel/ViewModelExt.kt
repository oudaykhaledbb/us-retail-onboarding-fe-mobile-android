package com.backbase.android.flow.common.viewmodel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.backbase.android.flow.common.state.State
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * All View model procedures require the same action when it comes to all states except success state
 * Idle, Failed and Incomplete states require unblocking the UI
 * Working states require blocking UI
 * @param receiveChannel ReceiveChannel<State<T>>
 * @param onSuccess (result: T?) -> Unit, executed when Success state is received
 * @param onFailed ((exception: Exception) -> Unit)?, executed when Failed state is received
 * @param onBlockUI: (() -> Unit)?, executed when UI element's isEnabled property set false
 * @param onUnblockUI: (() -> Unit)?, executed when UI element's isEnabled property set true
 * @param vararg actionViews: View, UI elements that needs to be enabled/disabled while state changes
 */
fun <T> Fragment.handleStates(
    receiveChannel: ReceiveChannel<State<T>>,
    onSuccess: (result: T?) -> Unit,
    onFailed: ((exception: Exception) -> Unit)? = null,
    onBlockUI: (() -> Unit)? = null,
    onUnblockUI: (() -> Unit)? = null,
    vararg actionViews: View
) = handleStates(receiveChannel, onSuccess, onFailed, onBlockUI, onUnblockUI, actionViews.toList())

/**
 * All View model procedures require the same action when it comes to all states except success state
 * Idle, Failed and Incomplete states require unblocking the UI
 * Working states require blocking UI
 * @param receiveChannel ReceiveChannel<State<T>>
 * @param onSuccess (result: T?) -> Unit, executed when Success state is received
 * @param onFailed ((exception: Exception) -> Unit)?, executed when Failed state is received
 * @param onBlockUI: (() -> Unit)?, executed when UI element's isEnabled property set false
 * @param onUnblockUI: (() -> Unit)?, executed when UI element's isEnabled property set true
 * @param actionViews: List<View>, UI elements that needs to be enabled/disabled while state changes
 */

fun <T> Fragment.handleStates(
    receiveChannel: ReceiveChannel<State<T>>,
    onSuccess: (result: T?) -> Unit,
    onFailed: ((exception: Exception) -> Unit)? = null,
    onBlockUI: (() -> Unit)? = null,
    onUnblockUI: (() -> Unit)? = null,
    actionViews: List<View>,
) {
    this.viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        for (state in receiveChannel) {
            when (state) {
                is State.Idle -> {
                    unblockViews(actionViews)
                    onUnblockUI?.invoke()
                }
                is State.Failed -> {
                    unblockViews(actionViews)
                    onUnblockUI?.invoke()
                    state.result?.let { onFailed?.let { it1 -> it1(it) } }
                }
                is State.Incomplete -> {
                    unblockViews(actionViews)
                    onUnblockUI?.invoke()
                }
                is State.Working -> {
                    blockViews(actionViews)
                    onBlockUI?.invoke()
                }
                is State.Success -> {
                    unblockViews(actionViews)
                    onUnblockUI?.invoke()
                    onSuccess(state.result)
                }
            }
        }
    }
}

private fun blockViews(actionViews: List<View>) {
    actionViews.forEach { view ->
        view.isEnabled = false
    }
}

private fun unblockViews(actionViews: List<View>) {
    actionViews.forEach { view ->
        view.isEnabled = true
    }
}