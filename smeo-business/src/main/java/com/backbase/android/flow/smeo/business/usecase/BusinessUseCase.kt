package com.backbase.android.flow.smeo.business.usecase

interface BusinessUseCase {

    suspend fun verifyCase(): Any?

    suspend fun submitBusinessDetails(
        legalName: String,
        knownName: String,
        ein: Integer,
        establishedDate: String,
        operationState: String
    ): Any?

}