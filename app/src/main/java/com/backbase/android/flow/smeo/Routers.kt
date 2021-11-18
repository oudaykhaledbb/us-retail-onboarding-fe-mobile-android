package com.backbase.android.flow.smeo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.backbase.android.flow.address.AddressRouter
import com.backbase.android.flow.businessrelations.BusinessRelationsRouter
import com.backbase.android.flow.common.interaction.InteractionResponse
import com.backbase.android.flow.identityverification.IdentityVerificationRouter
import com.backbase.android.flow.otp.OtpRouter
import com.backbase.android.flow.productselector.ProductSelectorRouter
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
import com.backbase.android.flow.smeo.landing.LandingScreen
import com.backbase.android.flow.smeo.walkthrough.WalkthroughRouter
import com.backbase.android.flow.ssn.SsnRouter
import com.backbase.android.flow.uploadfiles.UploadFilesRouter
import com.backbase.lookup.LookupRouter

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

fun otpRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : OtpRouter {

    override fun onOtpValidated(interactionResponse: com.backbase.android.flow.v2.models.InteractionResponse<*>?) {
        showJourneyWithClearStack(
            navController,
            R.id.productSelectionScreen
        )
        completion()
    }
}

fun productSelection(
    navController: NavController,
    completion: () -> Unit = {}
) = object : ProductSelectorRouter {

    override fun onProductSelectorFinished(interactionResponse: Any?) {
        showJourneyWithClearStack(
            navController,
            R.id.lookupJourney
        )
        completion()
    }

    override fun showHelpWhichProductToUse() {

    }

}

fun businessRelations(
    navController: NavController,
    completion: () -> Unit = {}
) = object : BusinessRelationsRouter {

    override fun onBusinessRelationsFinished() {
        showJourneyWithClearStack(
            navController,
            R.id.uploadDocumentsJourney
        )
        completion()
    }
}

fun showJourneyWithClearStack(navController: NavController, journeyScreenResId: Int) {
    val graph = navController.graph
    graph.startDestination = journeyScreenResId
    navController.graph = graph
}


fun lookupRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : LookupRouter {

    override fun onBusinessIdentityFinished(interactionResponse: Any?) {
        showJourneyWithClearStack(
            navController,
            R.id.businessRelationsJourneyScreen
        )
        completion()
    }

    override fun onSkipLookup(
        type: String,
        subtype: String?,
        interactionResponse: InteractionResponse<*>?
    ) {
        print("Lookup journey skipped")
        completion()
    }

}


fun addressRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : AddressRouter {
    override fun onAddressFinished(interactionResponse: com.backbase.android.flow.v2.models.InteractionResponse<*>?) {
        showJourneyWithClearStack(
            navController,
            R.id.SsnJourney
        )
        completion()
    }
}

fun ssnRouter(
    context: Context,
    completion: () -> Unit = {}
) = object : SsnRouter{


    override fun onSsnFinished(interactionResponse: com.backbase.android.flow.v2.models.InteractionResponse<Map<String, Any?>?>?) {
        val dialogFragment = LandingScreen()?.applyBundle(
                                interactionResponse?.body?.get("caseId")?.toString(),
                                interactionResponse?.body?.get("email")?.toString())
        dialogFragment.show((context as FragmentActivity).supportFragmentManager, "LandingScreen")
        completion()
    }

}

fun uploadFilesRouter(
    navController: NavController,
    completion: () -> Unit = {}
) = object : UploadFilesRouter {

    override fun onUploadFilesFinished() {
        showJourneyWithClearStack(
            navController,
            R.id.idvScreen
        )
        completion()
    }
}

fun idvRouter(
    navController: NavController,
    completion: () -> Unit = {}
) =
    object : IdentityVerificationRouter {
        override fun onIdentityVerified(interactionResponse: com.backbase.android.flow.v2.models.InteractionResponse<*>?) {
            showJourneyWithClearStack(
                navController,
                R.id.addressScreen
            )
            completion()
        }

        override fun onIdentityFailed(exception: Any?) {
            print("identity verification error")
        }
    }
