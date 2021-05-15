package com.backbase.android.flow.smeo.business.usecase

import android.content.Context
import com.backbase.android.flow.common.interaction.performInteraction
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.smeo.business.BusinessIdentityConfiguration
import com.backbase.android.flow.smeo.business.models.BusinessDetailsModel
import com.backbase.android.flow.smeo.business.models.IdentityModel
import com.google.gson.reflect.TypeToken

private const val JOURNEY_NAME = "sme-onboarding-identity"

class BusinessIdentityUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessIdentityConfiguration
) : BusinessIdentityUseCase {

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