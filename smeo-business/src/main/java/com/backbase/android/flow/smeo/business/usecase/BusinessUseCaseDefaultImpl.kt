package com.backbase.android.flow.smeo.business.usecase

import android.content.Context
import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.models.InteractionResponse
import com.backbase.android.flow.smeo.business.BusinessConfiguration
import com.backbase.android.flow.smeo.business.readAsset
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.util.*
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
        ein: Integer,
        establishedDate: String,
        operationState: String
    ) = if (configuration.isOffline) submitBusinessDetailsOffline(
        legalName,
        knownName,
        ein,
        establishedDate,
        operationState
    ) else submitBusinessDetailsOnline(
        legalName,
        knownName,
        ein,
        establishedDate,
        operationState
    )

    private suspend fun verifyCaseOnline() =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                flowClient.performInteraction(
                    Action("sme-onboarding-check-case-exist", null),
                    InteractionResponseHandler(continuation, "sme-onboarding-check-case-exist")
                )
            }
        }

    private suspend fun submitBusinessDetailsOnline(
        legalName: String,
        knownName: String,
        ein: Integer,
        establishedDate: String,
        operationState: String
    ) =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                val formData = HashMap<String, Serializable>()
                formData["legalName"] = legalName
                formData["knownName"] = knownName
                formData["ein"] = ein
                formData["establishedDate"] = establishedDate
                formData["operationState"] = operationState
                flowClient.performInteraction(
                    Action("sme-onboarding-business-details-data", formData),
                    InteractionResponseHandler(continuation, "sme-onboarding-business-details-data")
                )
            }
        }

    private suspend fun verifyCaseOffline() = Gson().fromJson(
        readAsset(
            context.assets,
            "backbase/smeo/check-case-exist.json"
        ), InteractionResponse::class.java
    )

    private suspend fun submitBusinessDetailsOffline(
        legalName: String,
        knownName: String,
        ein: Integer,
        establishedDate: String,
        operationState: String
    ) = Gson().fromJson(
        readAsset(
            context.assets,
            "backbase/smeo/business-details-data.json"
        ), InteractionResponse::class.java
    )

}