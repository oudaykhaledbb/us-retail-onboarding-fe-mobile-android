package com.backbase.android.flow.ssn.usecase

import com.backbase.android.flow.v2.models.InteractionResponse

interface SsnUsecase {
    suspend fun submitSsn(ssn: String): InteractionResponse<Map<String, Any?>?>?
    suspend fun landing(): InteractionResponse<Map<String, Any?>?>?
}