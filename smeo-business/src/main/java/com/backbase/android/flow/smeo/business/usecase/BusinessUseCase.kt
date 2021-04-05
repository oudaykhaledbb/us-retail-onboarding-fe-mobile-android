package com.backbase.android.flow.smeo.business.usecase

interface BusinessUseCase {

    suspend fun verifyCase(): Any?

    suspend fun submitBusinessDetails(
            legalName: String,
            knownName: String,
            ein: Int?,
            establishedDate: String,
            operationState: String
    ): Any?

    suspend fun submitBusinessIdentity(
            industry: String,
            businessDescription: String,
            companyWebsite: String?
    ): Any?

    suspend fun submitBusinessAddress(
            numberAndStreet: String,
            apt: String,
            city: String,
            state: String,
            zipCode: String
    ): Any?

}