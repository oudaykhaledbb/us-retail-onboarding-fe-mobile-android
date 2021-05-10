package com.backbase.android.flow.ssn.usecase

import android.content.Context
import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.common.utils.readAsset
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.models.InteractionResponse
import com.backbase.android.flow.ssn.SsnConfiguration
import com.backbase.android.flow.ssn.models.SsnRequest
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import kotlin.coroutines.suspendCoroutine

private const val JOURNEY_NAME = "sme-onboarding"

class SsnUsecaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: SsnConfiguration
) : SsnUsecase {

    override suspend fun submitSsn(ssn: String): Any? {
        submitPersonalAddress()// TODO code to be removed when Submit Personal Address Journey implemented
        val result = performInteraction<SsnRequest, Any?>(
            object : TypeToken<Any?>() {}.type,
            configuration.submitSsnAction,
            SsnRequest(ssn)// TODO to be discussed with BE team (Sina)
        )
        landing()
        return result
    }

    //TODO Remove below classes
    suspend fun submitPersonalAddress(): Any? {
        return performInteraction(
            object : TypeToken<Any?>() {}.type,
            "submit-address",
            PersonalAddressModel(
                "17 E Jefferson St",
                "41",
                22222,
                "Phoenix",
                "AZ",
                "Personal",
            )
        )
    }

    suspend fun landing(): Any? {
        return performInteraction<Any?, Any?>(
            object : TypeToken<Any?>() {}.type,
            "sme-onboarding-landing-data"
        )
    }

    //end TODO Remove below classes

    /**
     * This code should be moved to common library
     */

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
        return gson.fromJson<com.backbase.android.flow.common.interaction.InteractionResponse<Response?>>(
            rowResponse,
            type
        )?.body
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
                } as InteractionResponse?
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

//TODO Remove below classes
data class PersonalAddressModel(
    @SerializedName("numberAndStreet")
    val numberAndStreet: String,
    @SerializedName("apt")
    val apt: String,
    @SerializedName("zipCode")
    val zipCode: Int,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("addressName")
    val addressName: String
)
//end TODO Remove below classes