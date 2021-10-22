package com.backbase.android.flow.otp

import com.backbase.android.flow.v2.models.InteractionResponse

/**
 * Created by Backbase R&D B.V. on 2020-06-29.
 *
 * The exit points for the Otp Journey.
 */
interface OtpRouter {

    /**
     * Close OTP Journey
     */
    fun onOtpValidated(interactionResponse: InteractionResponse<*>?)
}