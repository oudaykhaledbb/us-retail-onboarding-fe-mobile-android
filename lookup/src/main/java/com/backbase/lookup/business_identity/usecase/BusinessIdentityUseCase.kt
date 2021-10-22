package com.backbase.lookup.business_identity.usecase

import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_identity.models.BusinessDetailsModel
import com.backbase.lookup.business_identity.models.Industry
import com.backbase.lookup.business_identity.models.IndustryCollectionModel

interface BusinessIdentityUseCase {

    suspend fun submitBusinessIdentity(
        industry: Industry,
        businessDescription: String?,
        companyWebsite: String?
    ): InteractionResponse<BusinessDetailsModel?>?

    suspend fun requestIndustries(): List<IndustryCollectionModel>

}