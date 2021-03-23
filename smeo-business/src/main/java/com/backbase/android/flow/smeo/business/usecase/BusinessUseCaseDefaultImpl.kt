package com.backbase.android.flow.smeo.business.usecase

import android.content.Context
import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.common.utils.readAsset
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.models.InteractionResponse
import com.backbase.android.flow.smeo.business.BusinessConfiguration
import com.backbase.android.flow.smeo.business.models.AddressModel
import com.backbase.android.flow.smeo.business.models.BusinessDetailsModel
import com.backbase.android.flow.smeo.business.models.IdentityModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

class BusinessUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessConfiguration
) : BusinessUseCase {

    override suspend fun verifyCase() =
        if (configuration.isOffline) verifyCaseOffline() else verifyCaseOnline()

    override suspend fun submitBusinessDetails(
            legalName: String,
            knownName: String,
            ein: Int?,
            establishedDate: String,
            operationState: String
    ) = if (configuration.isOffline) submitBusinessDetailsOffline()
    else submitBusinessDetailsOnline(
            legalName,
            knownName,
            ein,
            establishedDate,
            operationState
    )

    override suspend fun submitBusinessIdentity(industry: String, businessDescription: String, companyWebsite: String?) =
            if (configuration.isOffline) {
                submitBusinessIdentityOffline()
            } else {
                submitBusinessIdentityOnline(industry, businessDescription, companyWebsite)
            }

    override suspend fun submitBusinessAddress(numberAndStreet: String, apt: String, city: String, state: String, zipCode: String) =
            if (configuration.isOffline) {
                submitBusinessAddressOffline()
            } else {
                submitBusinessAddressOnline(numberAndStreet, apt, city, state, zipCode)
            }


    private suspend fun submitBusinessAddressOnline(numberAndStreet: String, apt: String, city: String, state: String, zipCode: String) =
            withContext(Dispatchers.Default) {
                suspendCoroutine<Any?> { continuation ->
                    val formData = AddressModel(
                        numberAndStreet,
                        apt,
                        city,
                        state,
                        zipCode
                    )
                    flowClient.performInteraction(
                        Action(configuration.submitBusinessAddressAction, formData),
                        InteractionResponseHandler(
                            continuation,
                            configuration.submitBusinessAddressAction
                        )
                    )
                }
            }

    suspend fun submitBusinessIdentityOnline(industry: String, businessDescription: String, companyWebsite: String?) =
            withContext(Dispatchers.Default) {
                suspendCoroutine<Any?> { continuation ->
                    val formData = IdentityModel(
                        industry,
                        businessDescription,
                        companyWebsite,
                        industry
                    )
                    flowClient.performInteraction(
                        Action(configuration.submitBusinessIdentityAction, formData),
                        InteractionResponseHandler(
                            continuation,
                            configuration.submitBusinessIdentityAction
                        )
                    )
                }
            }

    private suspend fun verifyCaseOnline() =
            withContext(Dispatchers.Default) {
                suspendCoroutine<Any?> { continuation ->
                    flowClient.performInteraction(
                        Action(configuration.verifyCaseAction, null),
                        InteractionResponseHandler(continuation, configuration.verifyCaseAction)
                    )
                }
            }

    private suspend fun submitBusinessDetailsOnline(
        legalName: String,
        knownName: String,
        ein: Int?,
        establishedDate: String,
        operationState: String
    ) =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                val formData = BusinessDetailsModel(
                    legalName,
                    knownName,
                    ein,
                    establishedDate,
                    operationState
                )
                flowClient.performInteraction(
                    Action(configuration.submitBusinessDetailsAction, formData),
                    InteractionResponseHandler(
                        continuation,
                        configuration.submitBusinessDetailsAction
                    )
                )
            }
        }

    private suspend fun verifyCaseOffline(): Any? {
        delay(30)
        return Gson().fromJson(
            readAsset(
                context.assets,
                "backbase/smeo/check-case-exist.json"
            ), InteractionResponse::class.java
        )
    }

    private suspend fun submitBusinessIdentityOffline(): Any? {
        delay(30)
        return null
    }

    private suspend fun submitBusinessAddressOffline() : Any? {
        delay(30)
        return null
    }

    private suspend fun submitBusinessDetailsOffline(): Any? {
        delay(30)
        return Gson().fromJson(
            readAsset(
                context.assets,
                "backbase/smeo/business-details-data.json"
            ), InteractionResponse::class.java
        )
    }

}