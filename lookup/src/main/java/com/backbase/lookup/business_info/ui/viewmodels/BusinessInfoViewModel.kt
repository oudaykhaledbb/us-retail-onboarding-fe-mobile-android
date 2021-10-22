package com.backbase.lookup.business_info.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_info.models.Item
import com.backbase.lookup.business_info.usecase.BusinessInfoUseCase

class BusinessInfoViewModel(private val useCase: BusinessInfoUseCase) : ViewModel() {

    internal val apiSubmitBusinessDetails = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiRequestBusinessStructure = ApiPublisher<List<Item>?>(this.viewModelScope)

    fun submitBusinessDetails(
        type: String,
        subType: String?,
        legalName: String,
        knownName: String,
        ein: Int?,
        establishedDate: String,
        operationState: String
    ) {
        apiSubmitBusinessDetails.submit("submitBusinessDetails()") {
            return@submit useCase.submitBusinessDetails(
                type,
                subType,
                legalName,
                knownName,
                ein,
                establishedDate,
                operationState
            )
        }
    }

    fun requestBusinessStructures(){
        apiRequestBusinessStructure.submit("requestBusinessStructures()") {
            return@submit useCase.requestBusinessStructures()
        }
    }
}