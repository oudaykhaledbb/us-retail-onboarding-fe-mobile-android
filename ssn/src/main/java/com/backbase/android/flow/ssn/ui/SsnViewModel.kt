package com.backbase.android.flow.ssn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.ssn.usecase.SsnUsecase
import com.backbase.android.flow.v2.models.InteractionResponse

class SsnViewModel(private val usecase: SsnUsecase) : ViewModel() {

    internal val apiSubmitSsn =
        ApiPublisher<InteractionResponse<Map<String, Any?>?>?>(this.viewModelScope)

    fun submitSsn(ssn: String) {
        apiSubmitSsn.submit("submitSsn()") {
            usecase.submitSsn(ssn)
            return@submit usecase.landing()
        }
    }

}
