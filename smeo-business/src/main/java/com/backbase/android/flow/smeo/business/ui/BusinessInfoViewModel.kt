package com.backbase.android.flow.smeo.business.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCase

class BusinessInfoViewModel(private val useCase: BusinessUseCase) : ViewModel() {

    internal val apiSubmitBusinessDetails = ApiPublisher<Any?>(this.viewModelScope)

    fun submitBusinessDetails(
        legalName: String,
        knownName: String,
        ein: Int?,
        establishedDate: String,
        operationState: String
    ) {
        apiSubmitBusinessDetails.submit("submitBusinessDetails()") {
            try {
                useCase.verifyCase()
                return@submit useCase.submitBusinessDetails(
                    legalName,
                    knownName,
                    ein,
                    establishedDate,
                    operationState
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}