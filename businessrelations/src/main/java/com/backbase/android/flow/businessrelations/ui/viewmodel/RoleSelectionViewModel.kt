package com.backbase.android.flow.businessrelations.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.businessrelations.PersistentData
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.model.UserInfo
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCase
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse
import com.google.gson.Gson


class RoleSelectionViewModel(private val useCase: BusinessRelationsUseCase) : ViewModel() {

    internal val apiSubmitRelationType = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)
    internal val apiCreateCase = ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)

    fun submitRelationType(relationType: RelationType) {
        apiSubmitRelationType.submit("submitRelationType()") {
            try {
                val relationTypeResponse = useCase.submitRelationType(relationType)           // select-relation-type //TODO Remove
                val businessPersonBody = useCase.requestBusinessPersons()
                PersistentData.currentUser = businessPersonBody?.body?.get(0)     // get-business-persons
                val businessRolesBody = useCase.requestBusinessRoles()
                    PersistentData.roles = businessRolesBody?.body                     // get-business-roles
                PersistentData.isCurrentUserTheControlPerson =
                    PersistentData.currentUser?.controlPerson == true
                return@submit relationTypeResponse
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@submit null
            }
        }
    }

    fun createCase(userInfo: UserInfo) {
        apiCreateCase.submit("createCase()") {
            return@submit useCase.createCase(userInfo)
        }
    }

}