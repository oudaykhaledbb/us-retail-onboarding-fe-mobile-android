package com.backbase.android.flow.smeo

import android.content.Context
import com.backbase.android.Backbase
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.FlowClient
import com.backbase.android.flow.address.AddressConfiguration
import com.backbase.android.flow.address.addressJourneyModule
import com.backbase.android.flow.address.usecase.AddressUseCase
import com.backbase.android.flow.address.usecase.AddressUseCaseDefaultImpl
import com.backbase.android.flow.businessrelations.BusinessRelationsConfiguration
import com.backbase.android.flow.businessrelations.BusinessRelationsJourneyModule
import com.backbase.android.flow.businessrelations.UserInfoProvider
import com.backbase.android.flow.businessrelations.model.UserInfo
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCase
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCaseDefaultImpl
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.otp.OtpConfiguration
import com.backbase.android.flow.otp.models.OtpChannel
import com.backbase.android.flow.otp.otpJourneyModule
import com.backbase.android.flow.otp.usecase.OtpUseCase
import com.backbase.android.flow.otp.usecase.OtpUseCaseDefaultImpl
import com.backbase.android.flow.productselector.ProductSelectorConfiguration
import com.backbase.android.flow.productselector.ProductSelectorJourneyModule
import com.backbase.android.flow.productselector.ProductSelectorUseCase
import com.backbase.android.flow.productselector.ProductSelectorUseCaseDefaultImpl
import com.backbase.android.flow.smeo.Constants.Companion.ABOUT_YOU_ACTION_INIT
import com.backbase.android.flow.smeo.Constants.Companion.ABOUT_YOU_ACTION_SUBMIT
import com.backbase.android.flow.smeo.Constants.Companion.DBS_PATH
import com.backbase.android.flow.smeo.Constants.Companion.INTERACTION_NAME
import com.backbase.android.flow.smeo.Constants.Companion.REQUEST_AVAILABLE_OTP_CHANNELS
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
import com.backbase.android.flow.smeo.business.businessIdentityConfiguration
import com.backbase.android.flow.smeo.business.businessIdentityJourneyModule
import com.backbase.android.flow.smeo.business.info.businessInfoConfiguration
import com.backbase.android.flow.smeo.business.info.businessInfoJourneyModule
import com.backbase.android.flow.smeo.business.info.usecase.BusinessInfoUseCase
import com.backbase.android.flow.smeo.business.info.usecase.BusinessInfoUseCaseDefaultImpl
import com.backbase.android.flow.smeo.business.usecase.BusinessIdentityUseCase
import com.backbase.android.flow.smeo.business.usecase.BusinessIdentityUseCaseDefaultImpl
import com.backbase.android.flow.smeo.walkthrough.walkthroughConfiguration
import com.backbase.android.flow.ssn.ssnConfiguration
import com.backbase.android.flow.ssn.ssnJourneyModule
import com.backbase.android.flow.ssn.usecase.SsnUsecase
import com.backbase.android.flow.ssn.usecase.SsnUsecaseDefaultImpl
import com.backbase.android.flow.stepnavigation.HeaderLabels
import com.backbase.android.flow.uploadfiles.uploadFilesConfiguration
import com.backbase.android.flow.uploadfiles.uploadJourneyModule
import com.backbase.android.flow.uploadfiles.usecase.UploadFilesUseCase
import com.backbase.android.flow.uploadfiles.usecase.UploadFilesUseCaseImpl
import com.backbase.deferredresources.DeferredText
import kotlinx.coroutines.delay
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.net.URI


var screenCounter = 0
val mapFragments = mapOf(
        "AboutYouJourney" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.nice_to_meet_you), DeferredText.Resource(R.string.personal_details)),
        "OtpJourney" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.security_at_your_fingertips), DeferredText.Resource(R.string.mobile_phone_number)),
        "BusinessRelationsJourneyScreen" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.the_business_owners), DeferredText.Resource(R.string.business_relations)),
        "ProductSelectionScreen" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.select_your_account_type), DeferredText.Resource(R.string.choose_product)),
        "BusinessInfoScreen" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.your_business_details), DeferredText.Resource(R.string.your_business)),
        "BusinessAddressScreen" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.where_is_your_business_located), DeferredText.Resource(R.string.your_business)),
        "BusinessIdentityScreen" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.what_does_your_company_do), DeferredText.Resource(R.string.your_business)),
        "UploadFilesJourney" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.verify_your_business), DeferredText.Resource(R.string.upload_documents)),
        "SsnJourney" to HeaderLabels(++screenCounter, DeferredText.Resource(R.string.verify_your_identity), DeferredText.Resource(R.string.your_ssn)),
)

/**
 * Koin module defining the app-level Authentication Journey configurations.
 */
val applicationModule = module {

    val baseUrl = "${Backbase.getInstance()?.configuration?.experienceConfiguration?.serverURL}"


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
                URI("$baseUrl/$DBS_PATH/$SERVICE_NAME"),
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
            isOffline = false
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
            availableOtpChannelsActionName = REQUEST_AVAILABLE_OTP_CHANNELS
            verificationCodeMaxLength = Integer(6)

        }
    }

    factory<OtpUseCase> {
        OtpUseCaseOffline(get(), OtpUseCaseDefaultImpl(get(), get()))
//        OtpUseCaseDefaultImpl(get(), get())
    }

    loadKoinModules(listOf(otpJourneyModule))
    //endregion OTP

    //region Business Identity
    factory {
        businessIdentityConfiguration {
            isOffline = false
            submitBusinessIdentityAction = SUBMIT_BUSINESS_IDENTITY_ACTION
        }
    }

    factory<BusinessIdentityUseCase> {
        BusinessIdentityUseCaseDefaultImpl(get(), get(), get())
    }

    loadKoinModules(listOf(businessIdentityJourneyModule))
    //endregion business Identity

    //region Business Info
    factory {
        businessInfoConfiguration {
            isOffline = false
            verifyCaseAction = VERIFY_CASE_ACTION
            submitBusinessDetailsAction = SUBMIT_BUSINESS_DETAILS_ACTION
        }
    }

    factory<BusinessInfoUseCase> {
        BusinessInfoUseCaseDefaultImpl(get(), get(), get())
    }

    loadKoinModules(listOf(businessInfoJourneyModule))
    //endregion Business Info

    //region Address Validation Journey
    factory {
        AddressConfiguration {
            actionName = "submit-address"
            description = DeferredText.Resource(R.string.label_we_need_to_know_you)
        }
    }

    factory<AddressUseCase> {
        return@factory AddressUseCaseDefaultImpl(get(), get())
    }


    loadKoinModules(listOf(addressJourneyModule))
    //endregion

    //region upload documents

    factory {
        uploadFilesConfiguration{
            isOffline = false
            supportedFiles = arrayListOf("pdf", "png", "jpg", "jpeg")
            requestDocumentAction = "load-document-requests"
            requestDataAction = "load-document-request"
            uploadDocumentAction = "upload-document"
            deleteTempDocumentAction = "delete-temp-document"
            submitDocumentAction = "submit-document-requests"
            completeTaskAction = "complete-task"
        }
    }

    factory<UploadFilesUseCase> {
        return@factory UploadFilesUseCaseImpl(get(), get(), get())
    }

    loadKoinModules(listOf(uploadJourneyModule))

    //endregion upload documents

    //region SSN
    factory {
        ssnConfiguration{
            isOffline = false
            submitSsnAction = "submit-address"
            landingAction = "sme-onboarding-landing-data"
        }
    }

    factory<SsnUsecase> {
        return@factory SsnUsecaseDefaultImpl(get(), get(), get())
    }

    loadKoinModules(listOf(ssnJourneyModule))
    //endregion SSN


    //region business relations
    loadKoinModules(listOf(BusinessRelationsJourneyModule))
    factory {

        BusinessRelationsConfiguration {
            isOffline = false
            createCase = "create-case"
            submitRelationTypeAction = "select-relation-type"
            updateOwnerAction = "update-owner"
            deleteOwnerAction = "delete-business-person"
            updateControlPersonAction = "update-control-person"
            deleteControlPersonAction = "delete-control-person"
            requestBusinessPersonsAction = "get-business-persons"
            submitControlPersonAction = "select-control-person"
            requestBusinessRolesAction = "get-business-roles"
            completeOwnersStepActionName = "complete-business-owners-step"
            completeControlPersonStepActionName = ""
            completeSummaryStepActionName = "complete-summary-step"
            userInfoProvider = object : UserInfoProvider {
                override fun getUserInfo() = UserInfo(
                    firstName = "Jack",
                    lastName = "Sparrow",
                    email = "jack.sparrow@gmail.com",
                    phoneNumber = "+12345678768"
                )
            }
        }
    }

    factory <BusinessRelationsUseCase>{
        return@factory BusinessRelationsUseCaseDefaultImpl(
            get(),
            get(),
            get()
        )
    }
    //endregion business relations


    //region Product Selector
    loadKoinModules(listOf(ProductSelectorJourneyModule))
    factory {
        ProductSelectorConfiguration{
            imageBaseUrl = baseUrl
            createCaseAction = "sme-onboarding-check-case-exist"
            requestProductsAction = "get-product-list"
            submitProductAction = "select-products"
        }
    }

    factory<ProductSelectorUseCase>{
        return@factory ProductSelectorUseCaseDefaultImpl(get(), get())
    }
    //endregion Product Selector


}

class OtpUseCaseOffline(
    private val context: Context,
    private val otpUseCaseDefaultImpl: OtpUseCaseDefaultImpl
) : OtpUseCase {
    override suspend fun requestAvailableOtpChannels() : List<OtpChannel> {
        delay(30)
        return listOf(OtpChannel.SMS, OtpChannel.EMAIL)
    }

    override suspend fun requestVerificationCode(phoneNumber: String, channel: OtpChannel) = otpUseCaseDefaultImpl.requestVerificationCode(phoneNumber, channel)

    override suspend fun submitVerificationCode(
        recipient: String,
        channel: OtpChannel,
        otp: String
    ) = otpUseCaseDefaultImpl.submitVerificationCode(recipient, channel, otp)
}


