package com.backbase.android.flow.ssn.usecase

import android.content.Context
import com.backbase.android.flow.common.interaction.performInteraction
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.ssn.SsnConfiguration
import com.backbase.android.flow.ssn.models.SsnModel
import com.google.gson.reflect.TypeToken

private const val JOURNEY_NAME = "sme-onboarding-ssn"

class SsnUsecaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: SsnConfiguration
) : SsnUsecase {

    override suspend fun submitSsn(ssn: String): Any? {
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