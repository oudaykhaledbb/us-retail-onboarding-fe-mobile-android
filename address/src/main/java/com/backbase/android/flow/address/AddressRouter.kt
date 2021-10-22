package com.backbase.android.flow.address

import com.backbase.android.flow.v2.models.InteractionResponse

/**
 * Created by Backbase R&D B.V. on 2020-06-29.
 *
 * The exit points for the Address Journey.
 */
interface AddressRouter {

    /**
     * Close Address Journey
     */
    fun onAddressFinished(interactionResponse: InteractionResponse<*>?)
}