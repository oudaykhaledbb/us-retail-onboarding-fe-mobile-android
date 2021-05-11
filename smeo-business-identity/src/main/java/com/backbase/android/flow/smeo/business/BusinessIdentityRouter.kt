package com.backbase.android.flow.smeo.business

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the SMEO Address.
 */
interface BusinessIdentityRouter {

    /**
     * Close SMEO Address Journey
     */
    fun onBusinessIdentityFinished()
}

