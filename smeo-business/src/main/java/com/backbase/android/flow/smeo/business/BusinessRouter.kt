package com.backbase.android.flow.smeo.business

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the SMEO Address.
 */
interface BusinessRouter {

    /**
     * Close SMEO Address Journey
     */
    fun onBusinessFinished()
}
