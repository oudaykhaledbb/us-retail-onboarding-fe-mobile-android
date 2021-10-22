package com.backbase.android.flow.identityverification

import com.backbase.android.flow.v2.models.InteractionResponse

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the Identity Verification Journey.
 */
interface IdentityVerificationRouter {
    fun onIdentityVerified(interactionResponse: InteractionResponse<*>?)
    fun onIdentityFailed(data: Any?)
}