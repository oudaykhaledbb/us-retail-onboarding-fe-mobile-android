package com.backbase.android.flow.businessrelations.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCase
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse


class SelectControlPersonViewModel(private val useCase: BusinessRelationsUseCase) : ViewModel() {
    internal val apiFetchOwners = ApiPublisher<InteractionResponse<List<Owner>?>?>(this.viewModelScope)
    internal val apiSelectControlPerson = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)

    fun requestBusinessPersons(relationType: RelationType? = null) {
        apiFetchOwners.submit("requestBusinessPersons()") {
            return@submit useCase.requestBusinessPersons(relationType)
        }
    }

    fun submitSelectedControlPerson(id: String) {
        apiSelectControlPerson.submit("submitSelectedControlPerson()") {
            return@submit useCase.submitControlPerson(id)
        }
    }

}