package com.backbase.android.flow.smeo.business.info.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.business.info.usecase.BusinessInfoUseCase

class BusinessInfoViewModel(private val useCase: BusinessInfoUseCase) : ViewModel() {

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