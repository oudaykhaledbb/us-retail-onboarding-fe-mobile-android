package com.backbase.android.flow.smeo.aboutyou.usecase

import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.suspendCoroutine

class AboutYouUseCaseDefaultImpl(
    private val flowClient: FlowClientContract,
    private val aboutYouConfiguration: AboutYouConfiguration
) : AboutYouUseCase {

    override suspend fun initSmeOnBoarding() =
        if (aboutYouConfiguration.isOffline) initSmeOnBoardingOffline() else initSmeOnBoardingOnline()

    override suspend fun submitAboutYou(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String
    ) =
        if (aboutYouConfiguration.isOffline) submitAboutYouOffline(
            firstName,
            lastName,
            dateOfBirth,
            email
        ) else submitAboutYouOnline(
            firstName,
            lastName,
            dateOfBirth,
            email
        )

    private suspend fun initSmeOnBoardingOnline() =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                val formData = HashMap<String, Boolean>()
                formData["accepted"] = true
                flowClient.performInteraction(
                    Action("sme-onboarding-init", formData),
                    InteractionResponseHandler(continuation, "sme-onboarding-init")
                )
            }
        }

    private suspend fun submitAboutYouOnline(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String
    ) =
        withContext(Dispatchers.Default) {
            suspendCoroutine<Any?> { continuation ->
                val formData = HashMap<String, String>()
                formData["firstName"] = firstName
                formData["lastName"] = lastName
                formData["dateOfBirth"] = dateOfBirth
                formData["emailAddress"] = email
                flowClient.performInteraction(
                    Action("sme-onboarding-anchor-data", formData),
                    InteractionResponseHandler(continuation, "sme-onboarding-anchor-data")
                )
            }
        }

    private suspend fun initSmeOnBoardingOffline() = null

    private suspend fun submitAboutYouOffline(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String
    ) = null

}
