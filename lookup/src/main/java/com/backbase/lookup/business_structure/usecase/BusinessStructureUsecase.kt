package com.backbase.lookup.business_structure.usecase

import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.business_structure.module.BusinessStructureResponseModel
import com.backbase.lookup.business_structure.module.Item

interface BusinessStructureUsecase {
    suspend fun createCase(): InteractionResponse<Map<String, Any?>?>?
    suspend fun requestBusinessStructures(): List<Item>
    suspend fun submitSelectedBusinessStructure(type: String, subtype: String?): InteractionResponse<BusinessStructureResponseModel?>?
}