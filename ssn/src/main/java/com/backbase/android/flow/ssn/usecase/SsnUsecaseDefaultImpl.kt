package com.backbase.android.flow.ssn.usecase

import android.content.Context
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.ssn.SsnConfiguration
import com.backbase.android.flow.ssn.models.SsnModel
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SsnUsecaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: SsnConfiguration
) : SsnUsecase {

    override suspend fun submitSsn(ssn: String) = flowClient.performInteraction<Map<String, Any?>?>(
        Action(configuration.submitSsnAction, SsnModel(ssn)),
        object : TypeToken<Map<String, Any?>?>() {}.type
    )


    override suspend fun landing(): InteractionResponse<Map<String, Any?>?>? {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        configuration.landingAction?.let {
            return flowClient.performInteraction(
                Action(it, null),
                responseType
            )
        }
        return null
    }


}