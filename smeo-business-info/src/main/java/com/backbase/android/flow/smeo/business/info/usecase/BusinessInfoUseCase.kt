package com.backbase.android.flow.smeo.business.info.usecase

interface BusinessInfoUseCase {

    suspend fun submitBusinessDetails(
            legalName: String,
            knownName: String,
            ein: Int?,
            establishedDate: String,
            operationState: String
    ): Any?
}