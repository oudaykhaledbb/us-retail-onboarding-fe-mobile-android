package com.backbase.android.flow.address.usecase

import com.backbase.android.flow.address.AddressConfiguration
import com.backbase.android.flow.address.models.AddressModel
import com.backbase.android.flow.common.v2.interaction.OnboardingBaseUseCaseDefaultImpl
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse

class AddressUseCaseDefaultImpl(
    private val flowClient: FlowClientContract,
    private val configuration: AddressConfiguration
) : OnboardingBaseUseCaseDefaultImpl(flowClient), AddressUseCase {

    override suspend fun fetchAddress(): InteractionResponse<OnboardingModel>? =
        submit(configuration.fetchActionName, "")

    override suspend fun submitAddress(payload: AddressModel): InteractionResponse<OnboardingModel>? =
        submit(configuration.submitActionName, payload)
}