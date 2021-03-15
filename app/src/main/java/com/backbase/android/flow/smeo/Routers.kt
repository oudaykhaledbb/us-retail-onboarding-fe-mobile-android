package com.backbase.android.flow.smeo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.navigation.NavController
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
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
        print("On About you finished")
    }
}

fun showJourneyWithClearStack(navController: NavController, journeyScreenResId: Int) {
    val graph = navController.graph
    graph.startDestination = journeyScreenResId
    navController.graph = graph
}