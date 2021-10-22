package com.backbase.lookup.business_structure.usecase

import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_structure.module.*

interface LookupUsecase {
    suspend fun requestCountries(): List<Country>?
    suspend fun requestCompanyLookup(lookupModel: LookupModel): InteractionResponse<List<BusinessModel>?>?
    suspend fun submitCompanyDetails(companyDetailsModel: CompanyDetailsModel): InteractionResponse<BusinessDetailsResponseModel?>?
}