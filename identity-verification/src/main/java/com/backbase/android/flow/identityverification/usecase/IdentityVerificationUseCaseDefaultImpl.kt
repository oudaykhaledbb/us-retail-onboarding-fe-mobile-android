package com.backbase.android.flow.identityverification.usecase

import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.common.v2.interaction.OnboardingBaseUseCaseDefaultImpl
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.identityverification.IdentityVerificationConfiguration
import com.backbase.android.flow.v2.models.InteractionResponse
import com.google.gson.reflect.TypeToken

class IdentityVerificationUseCaseDefaultImpl(
    flowClient: FlowClientContract,
    private val configuration: IdentityVerificationConfiguration
) : OnboardingBaseUseCaseDefaultImpl(flowClient), IdentityVerificationUseCase {

    override suspend fun initializeIdentityVerification(): InteractionResponse<Map<String, String>>? =
        submit(configuration.initiationActionName, null, object : TypeToken<Map<String, String>>() {}.type)

    override suspend fun submitIdentityVerification(verificationReference: String?): InteractionResponse<OnboardingModel>? {
        val payload = mutableMapOf<String, String>()
        verificationReference?.let { payload["verificationId"] = it }
        return submit(configuration.verificationActionName, payload)
    }
}