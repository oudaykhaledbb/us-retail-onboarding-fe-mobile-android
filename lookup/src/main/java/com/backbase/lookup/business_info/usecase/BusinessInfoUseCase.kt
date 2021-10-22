package com.backbase.lookup.business_info.usecase

import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_info.models.Item

interface BusinessInfoUseCase {

    suspend fun submitBusinessDetails(
            type: String,
            subType: String?,
            legalName: String,
            knownName: String,
            ein: Int?,
            establishedDate: String,
            operationState: String
    ): InteractionResponse<Map<String, Any?>?>?

    suspend fun requestBusinessStructures(): List<Item>
}