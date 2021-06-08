package com.backbase.android.flow.ssn.usecase

import com.backbase.android.flow.ssn.models.LandingModel

interface SsnUsecase {
    suspend fun submitSsn(ssn: String): LandingModel?
}