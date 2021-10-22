package com.backbase.android.flow.businessrelations.usecase

import android.content.Context
import com.backbase.android.flow.businessrelations.BusinessRelationsConfiguration
import com.backbase.android.flow.businessrelations.model.BusinessRoleModel
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.model.UserInfo
import com.backbase.android.flow.businessrelations.readAsset
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.android.flow.v2.throwExceptionIfErrorOrNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val JOURNEY_NAME = "business-relations"

class BusinessRelationsUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val businessRelationsConfiguration: BusinessRelationsConfiguration
) : BusinessRelationsUseCase {

    override suspend fun createCase(userInfo: UserInfo): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        businessRelationsConfiguration.createCaseActionName?.let {
            return flowClient.performInteraction<Map<String, Any?>?>(
                Action(it, userInfo),
                responseType
            ).throwExceptionIfErrorOrNull()
        }
        return null
    }

    override suspend fun submitRelationType(relationType: RelationType): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(
                businessRelationsConfiguration.submitRelationTypeActionName,
                hashMapOf(
                    "relationType" to relationType.toString()
                )
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun updateOwner(owner: Owner): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(businessRelationsConfiguration.updateOwnerActionName, owner),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun updateCurrentUserOwner(owner: Owner): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(businessRelationsConfiguration.updateCurrentUserOwnerActionName, owner),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun updateCurrentUserControlPerson(owner: Owner): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(businessRelationsConfiguration.updateCurrentUserControlPersonActionName, owner),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun deleteOwner(ownerID: String): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(
                businessRelationsConfiguration.deleteOwnerActionName, hashMapOf(
                    "id" to ownerID
                )
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun updateControlPerson(controlPersonUpdateRequest: Owner): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(
                businessRelationsConfiguration.updateControlPersonActionName,
                controlPersonUpdateRequest
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun deleteControlPerson(controlPersonID: String): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(
                businessRelationsConfiguration.deleteControlPersonActionName, hashMapOf(
                    "id" to controlPersonID
                )
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun requestBusinessPersons(relationType: RelationType?): InteractionResponse<List<Owner>?>? {
        val responseType: Type =
            object : TypeToken<List<Owner>?>() {}.type
        return flowClient.performInteraction<List<Owner>?>(
            Action(
                businessRelationsConfiguration.requestBusinessPersonsActionName,
                if (relationType == null) hashMapOf<String, String?>(
                    "relationType" to null
                ) else hashMapOf<String, String?>(
                    "relationType" to relationType?.toString()
                )
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun submitControlPerson(controlPersonId: String): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(
                businessRelationsConfiguration.submitControlPersonActionName, hashMapOf(
                    "id" to controlPersonId
                )
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun requestBusinessRoles(): InteractionResponse<List<BusinessRoleModel>?>? {
        val responseType: Type =
            object : TypeToken<List<BusinessRoleModel>?>() {}.type
        return flowClient.performInteraction<List<BusinessRoleModel>?>(
            Action(businessRelationsConfiguration.requestBusinessRolesActionName, null),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun completeOwnersStep() =
        performCompletionStep(businessRelationsConfiguration.completeOwnersStepActionName).throwExceptionIfErrorOrNull()

    override suspend fun completeControlPersonStep() =
        performCompletionStep(businessRelationsConfiguration.completeControlPersonStepActionName).throwExceptionIfErrorOrNull()

    override suspend fun completeSummaryStep() =
        performCompletionStep(businessRelationsConfiguration.completeSummaryStepActionName).throwExceptionIfErrorOrNull()

    private suspend fun <Request, Response> performInteraction(
        type: Type,
        action: String,
        request: Request? = null
    ): Response? =
        if (businessRelationsConfiguration.isOffline) performInteractionOffline<Response>(
            type,
            action
        ) else performInteractionOnline<Request, Response>(
            type,
            action,
            request
        )

    private suspend fun performCompletionStep(
        action: String
    ): InteractionResponse<Map<String, Any?>?>? {

        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type

        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(action, null),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    private fun <Response> performInteractionOffline(
        type: Type,
        action: String
    ): Response? {
        val gson = Gson()
        val rowResponse = readAsset(
            context.assets,
            "backbase/conf/$JOURNEY_NAME/$action.json"
        )
        return gson.fromJson<InteractionResponse<Response?>>(rowResponse, type)?.body
    }

    private suspend fun <Request, Response> performInteractionOnline(
        type: Type,
        action: String,
        request: Request? = null
    ): Response? {

        return flowClient.performInteraction<Response?>(
            Action(action, request),
            type
        ).throwExceptionIfErrorOrNull()?.body

    }

}

fun <T> Any.autoConvertTo(type: Type): T {
    val gson = Gson()
    return gson.fromJson<T>(gson.toJson(this), type)
}