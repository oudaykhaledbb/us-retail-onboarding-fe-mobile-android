package com.backbase.android.flow.common.handler

import com.backbase.android.flow.models.InteractionResponse

class InteractionResponseException(interactionResponse: InteractionResponse): Exception(
    convertActionErrorToString(interactionResponse)
)

private fun convertActionErrorToString(interactionResponse: InteractionResponse): String {
    val builder = StringBuilder()
    builder.append("An error occurred while calling Interaction SDK :: interactionID = ${interactionResponse.interactionId}")
    interactionResponse.actionErrors.forEach {
        builder.append("${it.message}\n")
    }
    return builder.toString()
}