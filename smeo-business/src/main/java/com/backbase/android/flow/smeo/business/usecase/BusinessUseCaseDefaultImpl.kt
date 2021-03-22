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
                    val formData = HashMap<String, Serializable?>()
                    formData["numberAndStreet"] = numberAndStreet
                    formData["apt"] = apt
                    formData["city"] = city
                    formData["state"] = state
                    formData["zipCode"] = zipCode
                    flowClient.performInteraction(
                            Action("sme-onboarding-business-address", formData),
                            InteractionResponseHandler(continuation, "sme-onboarding-business-address")
                    )
                }
            }

    suspend fun submitBusinessIdentityOnline(industry: String, businessDescription: String, companyWebsite: String?) =
            withContext(Dispatchers.Default) {
                suspendCoroutine<Any?> { continuation ->
                    val formData = HashMap<String, Serializable?>()
                    formData["industryKey"] = industry
                    formData["nature"] = businessDescription
                    formData["website"] = companyWebsite
                    formData["industryValue"] = industry
                    flowClient.performInteraction(
                            Action("sme-onboarding-business-identity-data", formData),
                            InteractionResponseHandler(continuation, "sme-onboarding-business-identity-data")
                    )
                }
            }

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
        ein: Int?,
        establishedDate: String,
        operationState: String
    ) =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                val formData = HashMap<String, Serializable?>()
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

    private fun verifyCaseOffline() = Gson().fromJson(
            readAsset(
                    context.assets,
                    "backbase/smeo/check-case-exist.json"
            ), InteractionResponse::class.java
    )

    private fun submitBusinessIdentityOffline() = null

    private fun submitBusinessAddressOffline() = null

    private fun submitBusinessDetailsOffline() = Gson().fromJson(
            readAsset(
                    context.assets,
                    "backbase/smeo/business-details-data.json"
            ), InteractionResponse::class.java
    )

}