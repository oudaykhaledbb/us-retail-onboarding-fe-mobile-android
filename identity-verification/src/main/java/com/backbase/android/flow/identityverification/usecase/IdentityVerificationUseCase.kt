package com.backbase.android.flow.identityverification.usecase

import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.models.InteractionResponse

interface IdentityVerificationUseCase {
    suspend fun initializeIdentityVerification(): InteractionResponse<Map<String,String>>?
    suspend fun submitIdentityVerification(verificationReference: String?): InteractionResponse<OnboardingModel>?
}