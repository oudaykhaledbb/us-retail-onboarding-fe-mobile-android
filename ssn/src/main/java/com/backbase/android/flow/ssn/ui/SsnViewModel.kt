package com.backbase.android.flow.ssn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.ssn.models.LandingModel
import com.backbase.android.flow.ssn.usecase.SsnUsecase

class SsnViewModel(private val usecase: SsnUsecase) : ViewModel() {

    internal val apiSubmitSsn = ApiPublisher<LandingModel?>(this.viewModelScope)

    fun submitSsn(
        ssn: String
    ) {
        apiSubmitSsn.submit("submitSsn()") {
                return@submit usecase.submitSsn(ssn)
        }
    }

}
