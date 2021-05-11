package com.backbase.android.flow.ssn.usecase

import android.content.Context
import com.backbase.android.flow.common.interaction.performInteraction
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.ssn.SsnConfiguration
import com.backbase.android.flow.ssn.models.SsnModel
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

private const val JOURNEY_NAME = "sme-onboarding-ssn"

class SsnUsecaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: SsnConfiguration
) : SsnUsecase {

    override suspend fun submitSsn(ssn: String): Any? {
        submitPersonalAddress()// TODO code to be removed when Submit Personal Address Journey implemented
        val result = performInteraction<SsnModel, Any?>(
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
            object : TypeToken<Any?>() {}.type,
            configuration.submitSsnAction,
            SsnModel(ssn)// TODO to be discussed with BE team (Sina)
        )
        landing()
        return result
    }

    //TODO Remove below classes
    suspend fun submitPersonalAddress(): Any? {
        return performInteraction(
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
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
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
            object : TypeToken<Any?>() {}.type,
            "sme-onboarding-landing-data"
        )
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