package com.backbase.android.flow.ssn

import com.backbase.android.flow.ssn.models.LandingModel
import com.backbase.android.flow.v2.models.InteractionResponse

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the SSN Journey.
 */
interface SsnRouter {

    /**
     * Close SSN Journey
     */
    fun onSsnFinished(interactionResponse: InteractionResponse<Map<String, Any?>?>?)
}