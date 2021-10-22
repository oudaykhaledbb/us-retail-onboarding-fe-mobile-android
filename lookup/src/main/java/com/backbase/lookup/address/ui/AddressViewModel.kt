package com.backbase.lookup.address.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.lookup.address.models.AddressModel
import com.backbase.lookup.address.usecase.LookupAddressUseCase
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.models.InteractionResponse

class AddressViewModel(private val useCase: LookupAddressUseCase) : ViewModel() {
    internal val apiSubmitAddress = ApiPublisher<InteractionResponse<OnboardingModel>?>(this.viewModelScope)

    fun submitAddress(payload: AddressModel) {
        apiSubmitAddress.submit {
            useCase.submitAddress(payload)
        }
    }

}