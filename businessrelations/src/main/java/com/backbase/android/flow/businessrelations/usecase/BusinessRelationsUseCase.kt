package com.backbase.android.flow.businessrelations.usecase

import com.backbase.android.flow.businessrelations.model.BusinessRoleModel
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.model.UserInfo
import com.backbase.android.flow.v2.models.InteractionResponse

interface BusinessRelationsUseCase {
    suspend fun createCase(userInfo: UserInfo): InteractionResponse<Map<String, Any?>?>?
    suspend fun submitRelationType(relationType: RelationType): InteractionResponse<Map<String, Any?>?>?
    suspend fun updateOwner(owner: Owner): InteractionResponse<Map<String, Any?>?>?
    suspend fun updateCurrentUserOwner(owner: Owner): InteractionResponse<Map<String, Any?>?>?
    suspend fun updateCurrentUserControlPerson(owner: Owner): InteractionResponse<Map<String, Any?>?>?
    suspend fun deleteOwner(ownerID: String): InteractionResponse<Map<String, Any?>?>?
    suspend fun updateControlPerson(controlPersonRequest: Owner): InteractionResponse<Map<String, Any?>?>?
    suspend fun deleteControlPerson(controlPersonID: String): InteractionResponse<Map<String, Any?>?>?
    suspend fun requestBusinessPersons(relationType: RelationType? = null): InteractionResponse<List<Owner>?>?
    suspend fun submitControlPerson(controlPersonId: String): InteractionResponse<Map<String, Any?>?>?
    suspend fun requestBusinessRoles(): InteractionResponse<List<BusinessRoleModel>?>?
    suspend fun completeOwnersStep(): InteractionResponse<Map<String, Any?>?>?
    suspend fun completeControlPersonStep(): InteractionResponse<Map<String, Any?>?>?
    suspend fun completeSummaryStep(): InteractionResponse<Map<String, Any?>?>?

}