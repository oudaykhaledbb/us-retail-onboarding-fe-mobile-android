package com.backbase.android.flow.smeo.business.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCase

class BusinessAddressScreenViewModel(private val useCase: BusinessUseCase) : ViewModel() {

    internal val apiSubmitBusinessAddress = ApiPublisher<Any?>(this.viewModelScope)

    fun submitBusinessAddress(
            numberAndStreet: String,
            apt: String,
            city: String,
            state: String,
            zipCode: String
    ) {
        apiSubmitBusinessAddress.submit("submitBusinessAddress()") {
            try {
                useCase.verifyCase()
                return@submit useCase.submitBusinessAddress(
                        numberAndStreet,
                        apt,
                        city,
                        state,
                        zipCode
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}