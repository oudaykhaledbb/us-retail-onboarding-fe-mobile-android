package com.backbase.lookup.business_structure.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_structure.module.*
import com.backbase.lookup.business_structure.usecase.LookupUsecase

class CompanyLookupViewModel(private val useCase: LookupUsecase): ViewModel() {

    internal val apiRequestCountries = ApiPublisher<List<Country>?>(this.viewModelScope)
    internal val apiRequestCompanyLookup = ApiPublisher<InteractionResponse<List<BusinessModel>?>?>(this.viewModelScope)
    internal val apiSubmitCompanyDetails = ApiPublisher<InteractionResponse<BusinessDetailsResponseModel?>?>(this.viewModelScope)

    fun requestCountries(){
        apiRequestCountries.submit("requestCountries()") {
            return@submit useCase.requestCountries()
        }
    }

    fun requestCompanyLookup(lookupModel: LookupModel){
        apiRequestCompanyLookup.submit("requestCompanyLookup()") {
            return@submit useCase.requestCompanyLookup(lookupModel)
        }
    }

    fun submitCompanyDetails(companyDetailsModel: CompanyDetailsModel){
        apiSubmitCompanyDetails.submit("submitCompanyDetails()") {
            return@submit useCase.submitCompanyDetails(companyDetailsModel)
        }
    }

}
