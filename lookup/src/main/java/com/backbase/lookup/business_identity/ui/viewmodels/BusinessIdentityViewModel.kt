package com.backbase.lookup.business_identity.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_identity.models.BusinessDetailsModel
import com.backbase.lookup.business_identity.models.Industry
import com.backbase.lookup.business_identity.models.IndustryCollectionModel
import com.backbase.lookup.business_identity.usecase.BusinessIdentityUseCase

class BusinessIdentityViewModel(private val useCase: BusinessIdentityUseCase) : ViewModel() {

    internal val apiSubmitBusinessIdentity = ApiPublisher<InteractionResponse<BusinessDetailsModel?>?>(this.viewModelScope)
    internal val apiRequestIdentities =
        ApiPublisher<List<IndustryCollectionModel>?>(this.viewModelScope)

    fun submitBusinessIdentity(
        industry: Industry,
        businessDescription: String?,
        companyWebsite: String?
    ) {
        apiSubmitBusinessIdentity.submit("submitBusinessIdentity") {
            return@submit useCase.submitBusinessIdentity(
                industry,
                businessDescription,
                companyWebsite
            )
        }
    }

    fun requestIndustries() {
        apiRequestIdentities.submit("requestIndustries") {
            return@submit useCase.requestIndustries()
        }
    }

}