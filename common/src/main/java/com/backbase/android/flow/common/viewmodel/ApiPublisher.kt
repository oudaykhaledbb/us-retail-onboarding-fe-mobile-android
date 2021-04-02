package com.backbase.android.flow.common.viewmodel

import com.backbase.android.core.utils.BBLogger
import com.backbase.android.flow.common.logging.TAG
import com.backbase.android.flow.common.state.State
import com.backbase.android.utils.net.response.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class ApiPublisher<T>(private val coroutineScope: CoroutineScope) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: ReceiveChannel<State<T>>
        get() = _broadcastChannel.openSubscription()

    @ExperimentalCoroutinesApi
    private val _broadcastChannel = BroadcastChannel<State<T>>(Channel.CONFLATED)

    private var currentState: State<T>? = null

    private fun notifyOnSubmitStateChanged(state: State<T> = State.Idle) {
        coroutineScope.launch {
            currentState = state

            @OptIn(ExperimentalCoroutinesApi::class)
            _broadcastChannel.send(state)
        }
    }

    fun submit(tag: String = TAG, apiExecutor: suspend (() -> T)) {
        if (currentState == State.Working) {
            BBLogger.warning(tag, "$tag called while already working: this call is ignored")
            return
        }
        coroutineScope.launch {
//            notifyOnSubmitStateChanged(State.Working)
            try {
                val response = apiExecutor.invoke()
                notifyOnSuccessStateChanged(response)
            } catch (ex: Exception) {
                notifyOnSubmitStateChanged(State.Failed(ex) as State<T>)
            }
        }
    }

    fun submitAsync(
        tag: String = TAG,
        apiExecutor: suspend ((continuation: (suspend ((Boolean, T?) -> Unit))) -> T),
    ) {
        if (currentState == State.Working) {
            BBLogger.warning(tag, "$tag called while already working: this call is ignored")
            return
        }
        coroutineScope.launch {
            notifyOnSubmitStateChanged(State.Working)
            try {
                apiExecutor.invoke { status, response ->
                    if (status) notifyOnSuccessStateChanged(response)
                    else {
                        var errorMessage: String? = "Unknown Error"
                        (response as? Response)?.let {
                            errorMessage = it.errorMessage
                        }
                        notifyOnSubmitStateChanged(State.Failed(Exception(errorMessage)) as State<T>)
                    }
                }
            } catch (ex: Exception) {
                notifyOnSubmitStateChanged(State.Failed(ex) as State<T>)
            }
        }
    }

    private fun notifyOnSuccessStateChanged(payload: T?) {
        notifyOnSubmitStateChanged(State.Success(payload))
//        notifyOnSubmitStateChanged(State.Idle)
    }
}