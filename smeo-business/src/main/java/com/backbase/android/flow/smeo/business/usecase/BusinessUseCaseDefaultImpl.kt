package com.backbase.android.flow.smeo.business.usecase

import android.content.Context
import com.backbase.android.flow.common.interaction.performInteraction
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.smeo.business.BusinessConfiguration
import com.backbase.android.flow.smeo.business.models.BusinessDetailsModel
import com.backbase.android.flow.smeo.business.models.IdentityModel
import com.google.gson.reflect.TypeToken

private const val JOURNEY_NAME = "sme-onboarding"

class BusinessUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessConfiguration
) : BusinessUseCase {

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
    ) = if (configuration.isOffline) submitBusinessDetailsOffline()
    else submitBusinessDetailsOnline(
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

    override suspend fun submitBusinessIdentity(industry: String, businessDescription: String, companyWebsite: String?): Any?{
        val requestModel = IdentityModel(
            industry,
            businessDescription,
            companyWebsite,
            industry
        )
        return performInteraction<Any, BusinessDetailsModel>(
            configuration.isOffline,
            context,
            JOURNEY_NAME,
            flowClient,
            object : TypeToken<BusinessDetailsModel>() {}.type,
            configuration.submitBusinessIdentityAction, requestModel
        )
    }

}