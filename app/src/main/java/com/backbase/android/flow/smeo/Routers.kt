package com.backbase.android.flow.smeo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.navigation.NavController
import com.backbase.android.flow.otp.OtpRouter
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
import com.backbase.android.flow.smeo.business.BusinessRouter
import com.backbase.android.flow.smeo.walkthrough.WalkthroughRouter

fun walkthroughRouter(
        context: Context,
        navController: NavController,
        completion: () -> Unit = {}
) = object :
        WalkthroughRouter {

    override fun onWalkthroughFinished() {
        (context as Activity).finish()
        context.startActivity(Intent(context, MainActivity::class.java))
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

fun aboutYouRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : AboutYouRouter {

    override fun onAboutYouFinished() {
        showJourneyWithClearStack(
            navController,
            R.id.otpJourney
        )
        completion()
    }
}

fun businessRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : BusinessRouter {

    override fun onBusinessFinished() {
        print("Business relations finished")
    }
}

fun otpRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : OtpRouter {

    override fun onOtpValidated(data: Any?) {
        showJourneyWithClearStack(
            navController,
            R.id.businessJourney
        )
        completion()
    }

}

fun showJourneyWithClearStack(navController: NavController, journeyScreenResId: Int) {
    val graph = navController.graph
    graph.startDestination = journeyScreenResId
    navController.graph = graph
}