package com.backbase.android.flow.common.handler

import android.util.Log
import com.backbase.android.flow.common.extensions.mapValues
import com.backbase.android.flow.common.logging.TAG
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.common.model.StepConfiguration
import com.backbase.android.flow.listeners.InteractionListener
import com.backbase.android.flow.models.ActionError
import com.backbase.android.flow.models.InteractionResponse
import com.backbase.android.utils.net.response.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class InteractionResponseHandler(
    private val continuation: Continuation<Any>,
    private var tagKey: String = ""
) :  InteractionListener<InteractionResponse> {

    override fun onSuccess(interactionResponse: InteractionResponse) {
        if (interactionResponse != null) {

            if(hasErrors(interactionResponse.actionErrors)){
                continuation.resumeWithException(Exception(interactionResponse.actionErrors.first().message))
                return
            }

            if (interactionResponse.body == null &&
                interactionResponse.step == null &&
                interactionResponse.interactionId != null
            ) {
                StepConfiguration.stepName = StepConfiguration.previousStepName
                StepConfiguration.previousStepName =
                    StepConfiguration.steps?.get(StepConfiguration.stepName)?.back
            }

            interactionResponse.body?.let {
                sync(it)
            }

            interactionResponse.step?.name?.let {
                StepConfiguration.previousStepName = StepConfiguration.stepName
                StepConfiguration.stepName = it
                Log.d(TAG, it)
            }

            interactionResponse.interactionId?.let { Log.d(TAG, it) }
            StepConfiguration.interactionId = interactionResponse.interactionId
            interactionResponse.steps?.let {
                StepConfiguration.steps = it
            }

            continuation.resume(interactionResponse)
        }
    }

    override fun onError(errorResponse: Response) {
        Log.d(TAG, "$TAG, $tagKey ==> ${errorResponse.errorMessage}")
        continuation.resumeWithException(Exception(errorResponse.errorMessage))
    }

    private fun sync(model: Any) {
        try {
            val gson = Gson()
            val onboardingModel = gson.toJson(model)
            Log.d(TAG, onboardingModel)
            val result = gson.fromJson<OnboardingModel?>(
                onboardingModel,
                object : TypeToken<OnboardingModel>() {}.type
            )
            result?.let {
                if (StepConfiguration.model == null)
                    StepConfiguration.model = it
                else {
                    (StepConfiguration.model as? Any)?.mapValues(it as? Any)
                }
            }
        } catch (ex: Exception) {
        }
    }

    private fun hasErrors(actionErrors: List<ActionError>?): Boolean {
        actionErrors?.let { return actionErrors.isNotEmpty() }
        return false
    }
}
