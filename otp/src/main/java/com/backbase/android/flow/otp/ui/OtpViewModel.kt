package com.backbase.android.flow.otp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.otp.models.OtpChannel
import com.backbase.android.flow.otp.usecase.OtpUseCase
import com.backbase.android.flow.v2.models.InteractionResponse

class OtpViewModel(private val useCase: OtpUseCase) : ViewModel() {
    internal val apiRequestVerificationCode = ApiPublisher<InteractionResponse<*>?>(this.viewModelScope)
    internal val apiSubmitVerificationCode = ApiPublisher<InteractionResponse<*>?>(this.viewModelScope)
    internal val apiRequestEmail = ApiPublisher<String?>(this.viewModelScope)
    internal val apiAvailableOtpChannels = ApiPublisher<List<OtpChannel>?>(this.viewModelScope)

    fun submitVerificationCode(recipient: String, channel: OtpChannel, otp: String) {
        apiSubmitVerificationCode.submit {
            useCase.submitVerificationCode(recipient, channel, otp)
        }
    }

    fun requestVerificationCode(recipient: String, channel: OtpChannel) {
        apiRequestVerificationCode.submit {
            useCase.requestVerificationCode(recipient, channel)
        }
    }

    fun requestAvailableOtpChannels() {
        apiAvailableOtpChannels.submit {
            useCase.requestAvailableOtpChannels()
        }
    }

    fun fetchEmail() {
        apiRequestEmail.submit {
            useCase.fetchOtpEmail()
        }
    }
}