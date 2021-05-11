package com.backbase.android.flow.ssn.usecase

interface SsnUsecase {
    suspend fun submitSsn(ssn: String): Any?
}