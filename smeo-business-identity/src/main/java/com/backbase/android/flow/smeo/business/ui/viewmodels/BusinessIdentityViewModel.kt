package com.backbase.android.flow.smeo.business.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.smeo.business.usecase.BusinessIdentityUseCase

class BusinessIdentityViewModel(private val useCase: BusinessIdentityUseCase) : ViewModel() {

    internal val apiSubmitBusinessIdentity = ApiPublisher<Any?>(this.viewModelScope)

    fun submitBusinessIdentity(
            legalName: String,
            knownName: String,
            companyWebsite: String?
    ) {
        apiSubmitBusinessIdentity.submit("submitBusinessIdentity") {
            try {
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