package com.backbase.android.flow.smeo.walkthrough

/**
 * Created by Backbase R&D B.V. on 2021-03-03.
 *
 * The exit points for the Walkthrough Journey.
 */
interface WalkthroughRouter {
    /**
     * Move to the next screen
     */
    fun onWalkthroughFinished()

    /**
     * Open terms and conditions screen
     */
    fun openTermsAndConditions()

    /**
     * Open privacy and policy screen
     */
    fun openPrivacyPolicy()
}