package com.backbase.android.flow.smeo.business.usecase

import android.content.Context
import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.common.utils.readAsset
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.smeo.business.BusinessConfiguration
import com.backbase.android.flow.smeo.business.models.BusinessDetailsModel
import com.backbase.android.flow.smeo.business.models.IdentityModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import kotlin.coroutines.suspendCoroutine

private const val JOURNEY_NAME = "sme-onboarding"

class BusinessUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessConfiguration
) : BusinessUseCase {

    override suspend fun verifyCase(): Any?{
        return performInteraction<Any, BusinessDetailsModel>(
            object : TypeToken<BusinessDetailsModel>() {}.type,
            configuration.verifyCaseAction
        )
    }

    override suspend fun submitBusinessDetails(
        legalName: String,
        knownName: String,
        ein: Int?,
        establishedDate: String,
        operationState: String
    ) : Any?{
        val formData = BusinessDetailsModel(
            legalName,
            knownName,
            ein,
            establishedDate,
            operationState
        )

        return performInteraction<Any, BusinessDetailsModel>(
            object : TypeToken<BusinessDetailsModel>() {}.type,
            configuration.submitBusinessDetailsAction, formData
        )
    }

    override suspend fun submitBusinessIdentity(industry: String, businessDescription: String, companyWebsite: String?): Any?{
        val requestModel = IdentityModel(
            industry,
            businessDescription,
            companyWebsite,
            industry
        )
        return performInteraction<Any, BusinessDetailsModel>(
            object : TypeToken<BusinessDetailsModel>() {}.type,
            configuration.submitBusinessIdentityAction, requestModel
        )
    }


    override suspend fun submitBusinessAddress(numberAndStreet: String, apt: String, city: String, state: String, zipCode: String): Any?{
        val requestModel = HashMap<String, String?>()
        requestModel["numberAndStreet"] = numberAndStreet
        requestModel["apt"] = apt
        requestModel["city"] = city
        requestModel["state"] = state
        requestModel["zipCode"] = zipCode
        return performInteraction<Any, BusinessDetailsModel>(
            object : TypeToken<BusinessDetailsModel>() {}.type,
            "sme-onboarding-business-address", requestModel
        )
    }

    private suspend fun <Request, Response> performInteraction(
        type: Type,
        action: String,
        request: Request? = null
    ): Response? =
        if (configuration.isOffline) performInteractionOffline<Response>(
            type,
            action
        ) else performInteractionOnline<Request, Response>(
            type,
            action,
            request
        )

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
        val rawResponse: com.backbase.android.flow.models.InteractionResponse?
        rawResponse = withContext(Dispatchers.Default) {
            try {
                suspendCoroutine<Any?> { continuation ->
                    flowClient.performInteraction(
                        Action(action, request),
                        InteractionResponseHandler(
                            continuation,
                            action
                        )
                    )
                } as com.backbase.android.flow.models.InteractionResponse?
            } catch (ex: Exception) {
                ex.printStackTrace()
                return@withContext null
            }

        }
        return rawResponse?.body?.autoConvertTo(type)
    }

    fun <T> Any.autoConvertTo(type: Type): T {
        val gson = Gson()
        return gson.fromJson<T>(gson.toJson(this), type)
    }

}