package com.backbase.android.flow.ssn.usecase

import android.content.Context
import com.backbase.android.flow.common.interaction.performInteraction
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.ssn.SsnConfiguration
import com.backbase.android.flow.ssn.models.LandingModel
import com.backbase.android.flow.ssn.models.SsnModel
import com.google.gson.reflect.TypeToken

private const val JOURNEY_NAME = "sme-onboarding-ssn"

class SsnUsecaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: SsnConfiguration
) : SsnUsecase {

    override suspend fun submitSsn(ssn: String): LandingModel? {
        performInteraction<SsnModel, Any?>(
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
            object : TypeToken<Any?>() {}.type,
            configuration.submitSsnAction,
            SsnModel(ssn)
        )
        return landing()
    }

    suspend fun landing(): LandingModel? {
        return performInteraction<Any?, LandingModel?>(
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
            object : TypeToken<LandingModel?>() {}.type,
            configuration.landingAction
        )
    }

}