package com.backbase.android.flow.smeo.business.info.usecase

import android.content.Context
import com.backbase.android.flow.common.interaction.performInteraction
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.smeo.business.info.BusinessInfoConfiguration
import com.backbase.android.flow.smeo.business.info.models.BusinessDetailsModel
import com.google.gson.reflect.TypeToken

private const val JOURNEY_NAME = "sme-onboarding_info"

class BusinessInfoUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessInfoConfiguration
) : BusinessInfoUseCase {

    override suspend fun verifyCase(): Any?{
        return performInteraction<Any, BusinessDetailsModel>(
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
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
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
            object : TypeToken<BusinessDetailsModel>() {}.type,
            configuration.submitBusinessDetailsAction, formData
        )
    }

}