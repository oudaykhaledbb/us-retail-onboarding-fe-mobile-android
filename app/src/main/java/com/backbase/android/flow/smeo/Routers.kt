package com.backbase.android.flow.smeo

import android.util.Log
import androidx.navigation.NavController
import com.backbase.android.flow.smeo.walkthrough.WalkthroughRouter

fun walkthroughRouter(
        navController: NavController,
        completion: () -> Unit = {}
) = object :
        WalkthroughRouter {

    override fun onWalkthroughFinished() {
        Log.d("walkthroughRouter", "Walkthrough finished")
        completion()
    }

    override fun openTermsAndConditions() {
        Log.d("walkthroughRouter", "terms and conditions")
        completion()
    }

    override fun openPrivacyPolicy() {
        Log.d("walkthroughRouter", "privacy and policy")
        completion()
    }
}