package com.backbase.android.flow.address.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.address.models.AddressModel
import com.backbase.android.flow.address.usecase.AddressUseCase
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse

class AddressViewModel(private val useCase: AddressUseCase) : ViewModel() {
    internal val apiSubmitAddress = ApiPublisher<InteractionResponse<OnboardingModel>?>(this.viewModelScope)
    internal val apiFetchAddress = ApiPublisher<InteractionResponse<OnboardingModel>?>(this.viewModelScope)

    fun submitAddress(payload: AddressModel) {
        apiSubmitAddress.submit {
            useCase.submitAddress(payload)
        }
    }

    fun fetchAddress() {
        apiFetchAddress.submit {
            useCase.fetchAddress()
        }
    }
}