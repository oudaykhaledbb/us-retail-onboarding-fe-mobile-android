package com.backbase.android.flow.ssn

import com.backbase.android.flow.ssn.models.LandingModel

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the SSN Journey.
 */
interface SsnRouter {

    /**
     * Close SSN Journey
     */
    fun onSsnFinished(it: LandingModel?)
}