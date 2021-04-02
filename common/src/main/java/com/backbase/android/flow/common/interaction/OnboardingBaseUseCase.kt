package com.backbase.android.flow.common.interaction

import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.models.InteractionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

interface OnboardingBaseUseCase {
    suspend fun submit(action: String, payload: Any?): InteractionResponse?
}

open class OnboardingBaseUseCaseDefaultImpl(private val flowClient: FlowClientContract) :
    OnboardingBaseUseCase {
    override suspend fun submit(actionName: String, payload: Any?): InteractionResponse? =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                flowClient.performInteraction(
                    Action(actionName, payload),
                    InteractionResponseHandler(continuation, actionName)
                )
            }
        } as? InteractionResponse
}