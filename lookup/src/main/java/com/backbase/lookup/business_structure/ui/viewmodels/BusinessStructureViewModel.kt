package com.backbase.lookup.business_structure.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_structure.module.BusinessStructureResponseModel
import com.backbase.lookup.business_structure.module.Item
import com.backbase.lookup.business_structure.usecase.BusinessStructureUsecase

class BusinessStructureViewModel(private val useCase: BusinessStructureUsecase) : ViewModel() {

    internal val apiCreateCase = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiRequestBusinessStructure = ApiPublisher<List<Item>?>(this.viewModelScope)
    internal val apiSubmitSelectedBusinessStructure = ApiPublisher<InteractionResponse<BusinessStructureResponseModel?>?>(this.viewModelScope)

    fun createCase(){
        apiCreateCase.submit("createCase()") {
            return@submit useCase.createCase()
        }
    }

    fun requestBusinessStructures(){
        apiRequestBusinessStructure.submit("requestBusinessStructures()") {
            useCase.createCase()
            return@submit useCase.requestBusinessStructures()
        }
    }

    fun submitSelectedBusinessStructure(type: String, subtype: String?){
        apiSubmitSelectedBusinessStructure.submit("submitSelectedBusinessStructure()") {
            return@submit useCase.submitSelectedBusinessStructure(type, subtype)
        }
    }

}