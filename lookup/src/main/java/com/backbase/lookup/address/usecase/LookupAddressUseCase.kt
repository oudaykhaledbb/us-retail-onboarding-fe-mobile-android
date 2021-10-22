package com.backbase.lookup.address.usecase

import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.address.models.AddressModel

interface LookupAddressUseCase {
    suspend fun submitAddress(payload: AddressModel): InteractionResponse<OnboardingModel>?
}
