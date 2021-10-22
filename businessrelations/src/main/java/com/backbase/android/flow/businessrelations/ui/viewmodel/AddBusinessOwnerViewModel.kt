package com.backbase.android.flow.businessrelations.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.businessrelations.model.BusinessRoleModel
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCase
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse


class AddBusinessOwnerViewModel(private val useCase: BusinessRelationsUseCase): ViewModel() {

    internal val apiFetchOwners = ApiPublisher<InteractionResponse<List<Owner>?>?>(this.viewModelScope)
    internal val apiFetchRoles = ApiPublisher<InteractionResponse<List<BusinessRoleModel>?>?>(this.viewModelScope)
    internal val apiUpdateOwner = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiUpdateControlPerson = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiDeleteOwner = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiDeleteControlPerson = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiCompleteOwnersStep = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiCompleteSummaryStep = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)

    fun requestBusinessPersons(relationType: RelationType? = null) {
        apiFetchOwners.submit("requestBusinessPersons()") {
            return@submit useCase.requestBusinessPersons(relationType)
        }
    }

    fun requestBusinessRolesAction() {
        apiFetchRoles.submit("requestBusinessRolesAction()") {
            return@submit useCase.requestBusinessRoles()
        }
    }

    fun updateOwner(owner: Owner, relationType: RelationType?){
        apiUpdateOwner.submit("updateOwner()"){
            return@submit when (relationType) {
                null -> useCase.updateOwner(owner)
                RelationType.OWNER -> useCase.updateCurrentUserOwner(owner)
                else -> useCase.updateCurrentUserControlPerson(owner)
            }
        }
    }

    fun updateControlPerson(owner: Owner){
        apiUpdateControlPerson.submit("updateControlPerson()"){
            return@submit useCase.updateControlPerson(owner)
        }
    }

    fun deleteOwner(id: String){
        apiDeleteOwner.submit("deleteOwner()"){
            return@submit useCase.deleteOwner(id)
        }
    }

    fun deleteControlPerson(id: String){
        apiDeleteControlPerson.submit("deleteControlPerson()"){
            return@submit useCase.deleteControlPerson(id)
        }
    }

    fun completeOwnersStepActionName(){
        apiCompleteOwnersStep.submit("completeOwnersStepActionName()"){
            return@submit useCase.completeOwnersStep()
        }
    }

    fun completeSummaryStep(){
        apiCompleteSummaryStep.submit("completeSummaryStep()"){
            return@submit useCase.completeSummaryStep()
        }
    }

}