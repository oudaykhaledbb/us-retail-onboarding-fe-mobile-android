package com.backbase.lookup

import com.backbase.android.flow.common.interaction.InteractionResponse

interface LookupRouter {

    /**
     * Close SMEO Address Journey
     */
    fun onBusinessIdentityFinished(interactionResponse: Any?)

    /**
     * Skip lookup journey
     */
    fun onSkipLookup(
        type: String,
        subtype: String?,
        interactionResponse: InteractionResponse<*>?
    )

}