package com.backbase.android.flow.smeo.aboutyou.usecase

import com.backbase.android.flow.models.Action
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import com.backbase.android.flow.smeo.aboutyou.models.AboutYouModel
import com.backbase.android.flow.smeo.aboutyou.models.InitSmeModel
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.throwExceptionIfErrorOrNull
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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

    private suspend fun initSmeOnBoardingOnline() {
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        flowClient.performInteraction<Map<String, Any?>?>(
            Action(aboutYouConfiguration.actionInit, InitSmeModel(true)), responseType
        ).throwExceptionIfErrorOrNull()
    }

    private suspend fun submitAboutYouOnline(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String
    ) {
        val model = AboutYouModel(
            firstName,
            lastName,
            dateOfBirth,
            email
        )
        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type

            flowClient.performInteraction<Map<String, Any?>?>(
                Action(aboutYouConfiguration.actionAboutYou, model),
                responseType
            ).throwExceptionIfErrorOrNull()
    }

    private suspend fun initSmeOnBoardingOffline() = null

    private suspend fun submitAboutYouOffline(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String
    ) = null

}