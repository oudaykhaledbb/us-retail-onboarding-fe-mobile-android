package com.backbase.android.flow.smeo.aboutyou.usecase

import com.backbase.android.flow.common.handler.InteractionResponseHandler
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import com.backbase.android.flow.smeo.aboutyou.models.AboutYouModel
import com.backbase.android.flow.smeo.aboutyou.models.InitSmeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                flowClient.performInteraction(
                    Action(aboutYouConfiguration.actionInit, InitSmeModel(true)),
                    InteractionResponseHandler(continuation, aboutYouConfiguration.actionInit)
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
                val model = AboutYouModel(
                    firstName,
                    lastName,
                    dateOfBirth,
                    email
                )
                flowClient.performInteraction(
                    Action(aboutYouConfiguration.actionAboutYou, model),
                    InteractionResponseHandler(continuation, aboutYouConfiguration.actionAboutYou)
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
