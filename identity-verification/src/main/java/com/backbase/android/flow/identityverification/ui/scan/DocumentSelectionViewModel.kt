package com.backbase.android.flow.identityverification.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.identityverification.IdentityVerificationViewModel
import com.jumio.nv.custom.NetverifyCountry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class DocumentSelectionViewModel : ViewModel(),
    IdentityVerificationViewModel.RequestCountriesListener {

    // Country Request State Region
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: ReceiveChannel<State<*>>
        get() = _broadcastChannel.openSubscription()

    @ExperimentalCoroutinesApi
    private val _broadcastChannel = BroadcastChannel<State<*>>(Channel.CONFLATED)

    private var currentState: State<*>? = null

    private fun notifyOnSubmitStateChanged(state: State<*> = State.Idle) {
        viewModelScope.launch {
            currentState = state

            @OptIn(ExperimentalCoroutinesApi::class)
            _broadcastChannel.send(state)
        }
    }
    //region end

    /**
     * initializeNetverifySDK function must be called before
     */
    fun requestCountries(identityVerificationViewModel: IdentityVerificationViewModel) {
        identityVerificationViewModel.requestCountries(this)
    }

    override fun onCountriesFailed() {
        notifyOnSubmitStateChanged(State.Failed(Exception()))
    }

    override fun onCountriesRetreivedSuccessfully(countries: Map<String, NetverifyCountry>?) {
        notifyOnSubmitStateChanged(State.Success(countries))
        notifyOnSubmitStateChanged(State.Idle)
    }
}
