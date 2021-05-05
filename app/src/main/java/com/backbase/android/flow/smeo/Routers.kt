package com.backbase.android.flow.smeo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.navigation.NavController
import com.backbase.android.flow.address.AddressRouter
import com.backbase.android.flow.otp.OtpRouter
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
import com.backbase.android.flow.smeo.business.BusinessRouter
import com.backbase.android.flow.smeo.walkthrough.WalkthroughRouter
import com.backbase.android.flow.ssn.SsnRouter
import com.backbase.android.flow.uploadfiles.UploadFilesRouter

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
        showJourneyWithClearStack(
            navController,
            R.id.uploadDocumentsJourney
        )
        completion()
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

fun addressRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : AddressRouter {

    override fun onAddressFinished(data: Any?) {
        navController.navigate(com.backbase.android.flow.smeo.business.R.id.action_to_businessIdentityScreen)
        completion()
    }
}

fun ssnRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : SsnRouter{

    override fun onSsnFinished() {
        print("SSN finished")
        completion()
    }
}

fun uploadFilesRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : UploadFilesRouter{

    override fun onUploadFilesFinished() {
        showJourneyWithClearStack(
            navController,
            R.id.SsnJourney
        )
        completion()
    }
}