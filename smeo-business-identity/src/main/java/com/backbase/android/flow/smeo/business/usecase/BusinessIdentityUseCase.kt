package com.backbase.android.flow.smeo.business.usecase

interface BusinessIdentityUseCase {

    suspend fun submitBusinessIdentity(
            industry: String,
            businessDescription: String,
            companyWebsite: String?
    ): Any?

}