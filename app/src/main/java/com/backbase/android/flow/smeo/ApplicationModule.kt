package com.backbase.android.flow.smeo

import android.content.Context
import com.backbase.android.Backbase
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.FlowClient
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.InteractionResponse
import com.backbase.android.flow.otp.OtpConfiguration
import com.backbase.android.flow.otp.otpJourneyModule
import com.backbase.android.flow.otp.usecase.Channel
import com.backbase.android.flow.otp.usecase.OtpUseCase
import com.backbase.android.flow.smeo.Constants.Companion.INTERACTION_NAME
import com.backbase.android.flow.smeo.Constants.Companion.SERVICE_NAME
import com.backbase.android.flow.smeo.Constants.Companion.DBS_PATH
import com.backbase.android.flow.smeo.Constants.Companion.REQUEST_OTP_ACTIONNAME
import com.backbase.android.flow.smeo.Constants.Companion.SUBMIT_BUSINESS_DETAILS_ACTION
import com.backbase.android.flow.smeo.Constants.Companion.VERIFYACTIONNAME
import com.backbase.android.flow.smeo.Constants.Companion.VERIFY_CASE_ACTION
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import com.backbase.android.flow.smeo.aboutyou.aboutYouJourneyModule
import com.backbase.android.flow.smeo.aboutyou.readAsset
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCase
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCaseDefaultImpl
import com.backbase.android.flow.smeo.business.BusinessConfiguration
import com.backbase.android.flow.smeo.business.businessJourneyModule
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCase
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCaseDefaultImpl
import com.backbase.android.flow.smeo.walkthrough.walkthroughConfiguration
import com.google.gson.Gson
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.net.URI

/**
 * Koin module defining the app-level Authentication Journey configurations.
 */
val applicationModule = module {

    factory {
        walkthroughConfiguration {
            pages = arrayListOf(welcomePage(), requirementsPage(), informationPage(), businessNotAllowedPage(), termsAndConditionsPages())
        }
    }

    single {
        NetworkDBSDataProvider(get())
    }

    single<FlowClientContract> {
        val dbsProvider: NetworkDBSDataProvider by inject()
        FlowClient(
                get(),
                URI("${Backbase.getInstance()?.configuration?.experienceConfiguration?.serverURL}/$DBS_PATH/$SERVICE_NAME"),
                dbsProvider,
                null,
                INTERACTION_NAME,
                null
        )
    }

    factory<AboutYouUseCase> {
        return@factory AboutYouUseCaseDefaultImpl(get(), get())
    }

    factory {
        AboutYouConfiguration {
            isOffline = true
        }
    }

    loadKoinModules(aboutYouJourneyModule)

    //region OTP
    factory {
        OtpConfiguration {
            requestActionName = REQUEST_OTP_ACTIONNAME
            verifyActionName = VERIFYACTIONNAME
        }
    }

    factory<OtpUseCase> {
        OtpUseCaseOffline(get())
//        OtpUseCaseDefaultImpl(get(), get())
    }

    loadKoinModules(listOf(otpJourneyModule))
    //endregion OTP

    //region OTP
    factory {
        BusinessConfiguration {
            isOffline = true
            verifyCaseAction = VERIFY_CASE_ACTION
            submitBusinessDetailsAction = SUBMIT_BUSINESS_DETAILS_ACTION
        }
    }

    factory<BusinessUseCase> {
        BusinessUseCaseDefaultImpl(get(), get(), get())
    }

    loadKoinModules(listOf(businessJourneyModule))
    //endregion OTP

}

class OtpUseCaseOffline(private val context: Context) : OtpUseCase {
    override suspend fun requestVerificationCode(phoneNumber: String, channel: Channel) =
        Gson().fromJson(
            readAsset(
                context.assets,
                "backbase/otp/get-verification-code.json"
            ), InteractionResponse::class.java
        )

    override suspend fun submitVerificationCode(
        recipient: String,
        channel: Channel,
        otp: String
    ) = Gson().fromJson(
        readAsset(
            context.assets,
            "backbase/otp/submit-verification-code.json"
        ), InteractionResponse::class.java
    )
}