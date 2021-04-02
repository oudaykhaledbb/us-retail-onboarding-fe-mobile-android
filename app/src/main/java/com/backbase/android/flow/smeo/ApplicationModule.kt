package com.backbase.android.flow.smeo

import android.content.Context
import com.backbase.android.Backbase
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.FlowClient
import com.backbase.android.flow.address.AddressConfiguration
import com.backbase.android.flow.address.addressJourneyModule
import com.backbase.android.flow.address.models.FormItem
import com.backbase.android.flow.address.usecase.AddressUseCase
import com.backbase.android.flow.common.utils.readAsset
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.models.InteractionResponse
import com.backbase.android.flow.otp.OtpConfiguration
import com.backbase.android.flow.otp.models.OtpChannel
import com.backbase.android.flow.otp.otpJourneyModule
import com.backbase.android.flow.otp.usecase.OtpUseCase
import com.backbase.android.flow.smeo.Constants.Companion.ABOUT_YOU_ACTION_INIT
import com.backbase.android.flow.smeo.Constants.Companion.ABOUT_YOU_ACTION_SUBMIT
import com.backbase.android.flow.smeo.Constants.Companion.DBS_PATH
import com.backbase.android.flow.smeo.Constants.Companion.INTERACTION_NAME
import com.backbase.android.flow.smeo.Constants.Companion.REQUEST_OTP_ACTIONNAME
import com.backbase.android.flow.smeo.Constants.Companion.SERVICE_NAME
import com.backbase.android.flow.smeo.Constants.Companion.SUBMIT_BUSINESS_DETAILS_ACTION
import com.backbase.android.flow.smeo.Constants.Companion.SUBMIT_BUSINESS_IDENTITY_ACTION
import com.backbase.android.flow.smeo.Constants.Companion.VERIFYACTIONNAME
import com.backbase.android.flow.smeo.Constants.Companion.VERIFY_CASE_ACTION
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import com.backbase.android.flow.smeo.aboutyou.aboutYouJourneyModule
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCase
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCaseDefaultImpl
import com.backbase.android.flow.smeo.business.BusinessConfiguration
import com.backbase.android.flow.smeo.business.businessJourneyModule
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCase
import com.backbase.android.flow.smeo.business.usecase.BusinessUseCaseDefaultImpl
import com.backbase.android.flow.smeo.walkthrough.walkthroughConfiguration
import com.backbase.android.flow.stepnavigation.HeaderLabels
import com.backbase.deferredresources.DeferredText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.lang.reflect.Type
import java.net.URI


val mapFragments = mapOf(
        "AboutYouJourney" to HeaderLabels(1, DeferredText.Resource(R.string.nice_to_meet_you), DeferredText.Resource(R.string.personal_details)),
        "OtpJourney" to HeaderLabels(2, DeferredText.Resource(R.string.security_at_your_fingertips), DeferredText.Resource(R.string.mobile_phone_number)),
        "BusinessInfoScreen" to HeaderLabels(3, DeferredText.Resource(R.string.your_business_details), DeferredText.Resource(R.string.personal_details)),
        "BusinessAddressScreen" to HeaderLabels(4, DeferredText.Resource(R.string.where_is_your_business_located), DeferredText.Resource(R.string.your_business)),
        "BusinessIdentityScreen" to HeaderLabels(5, DeferredText.Resource(R.string.what_does_your_company_do), DeferredText.Resource(R.string.your_business))
)

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
            actionInit = ABOUT_YOU_ACTION_INIT
            actionAboutYouSubmit = ABOUT_YOU_ACTION_SUBMIT
        }
    }

    loadKoinModules(aboutYouJourneyModule)

    //region OTP
    factory {
        OtpConfiguration {
            requestActionName = REQUEST_OTP_ACTIONNAME
            verifyActionName = VERIFYACTIONNAME
            availableOtpChannelsActionName = ""
            verificationCodeMaxLength = Integer(9)

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
            submitBusinessIdentityAction = SUBMIT_BUSINESS_IDENTITY_ACTION
        }
    }

    factory<BusinessUseCase> {
        BusinessUseCaseDefaultImpl(get(), get(), get())
    }

    loadKoinModules(listOf(businessJourneyModule))
    //endregion OTP

    //region Address Validation Journey
    factory {
        AddressConfiguration {
            val context: Context by inject()
            val formItemsType: Type = object :
                TypeToken<ArrayList<FormItem>>() {}.type
            formItems = Gson().fromJson(
                readAsset(
                    context.assets,
                    "backbase/conf/smeo/address.json"
                ), formItemsType
            )
        }
    }

    factory<AddressUseCase> {
        return@factory object : AddressUseCase {
            override suspend fun submitAddress(formData: HashMap<String, String?>): Any?{
                val context: Context by inject()
                return readAsset(
                    context.assets,
                    "backbase/smeo/address.json"
                )
            }
        }
    }

    loadKoinModules(listOf(addressJourneyModule))
    //endregion

}

class OtpUseCaseOffline(private val context: Context) : OtpUseCase {
    override suspend fun requestAvailableOtpChannels() : List<OtpChannel> {
        delay(30)
        return listOf(OtpChannel.SMS, OtpChannel.EMAIL)
    }

    override suspend fun requestVerificationCode(phoneNumber: String, channel: OtpChannel): Any? {
        delay(30)
        return Gson().fromJson(
                readAsset(
                        context.assets,
                        "backbase/otp/get-verification-code.json"
                ), InteractionResponse::class.java
        )
    }

    override suspend fun submitVerificationCode(
        recipient: String,
        channel: OtpChannel,
        otp: String
    ): Any? {
        delay(30)
        return Gson().fromJson(
                readAsset(
                        context.assets,
                        "backbase/otp/submit-verification-code.json"
                ), InteractionResponse::class.java
        )
    }
}


