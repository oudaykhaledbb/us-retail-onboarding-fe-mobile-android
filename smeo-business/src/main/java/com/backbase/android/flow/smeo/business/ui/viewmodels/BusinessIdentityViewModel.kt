package com.backbase.android.flow.smeo.business.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCase

class BusinessIdentityViewModel(private val useCase: BusinessUseCase) : ViewModel() {

    internal val apiSubmitBusinessIdentity = ApiPublisher<Any?>(this.viewModelScope)

    fun submitBusinessIdentity(
            legalName: String,
            knownName: String,
            companyWebsite: String?
    ) {
        apiSubmitBusinessIdentity.submit("submitBusinessDetails()") {
            try {
                useCase.verifyCase()
                return@submit useCase.submitBusinessIdentity(
                        legalName,
                        knownName,
                        companyWebsite
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}