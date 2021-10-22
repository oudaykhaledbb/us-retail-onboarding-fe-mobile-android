package com.backbase.android.flow.address.usecase

import com.backbase.android.flow.address.models.AddressModel
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.models.InteractionResponse

interface AddressUseCase {
    suspend fun fetchAddress(): InteractionResponse<OnboardingModel>?
    suspend fun submitAddress(payload: AddressModel): InteractionResponse<OnboardingModel>?
}
