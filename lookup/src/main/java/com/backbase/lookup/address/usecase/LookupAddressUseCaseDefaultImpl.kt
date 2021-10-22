package com.backbase.lookup.address.usecase

import com.backbase.android.flow.common.v2.interaction.OnboardingBaseUseCaseDefaultImpl
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.LocalStorage
import com.backbase.lookup.address.LookupAddressConfiguration
import com.backbase.lookup.address.models.AddressModel

class LookupAddressUseCaseDefaultImpl(
    private val flowClient: FlowClientContract,
    private val configurationLookup: LookupAddressConfiguration,
    private val localAddressStorage: LocalStorage,
) : OnboardingBaseUseCaseDefaultImpl(flowClient), LookupAddressUseCase {

    override suspend fun submitAddress(payload: AddressModel): InteractionResponse<OnboardingModel>?{
        localAddressStorage.storeAddressModel(payload)
        return submit(configurationLookup.submitActionName, payload)
    }

}