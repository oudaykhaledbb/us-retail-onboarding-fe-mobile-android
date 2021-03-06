package com.backbase.android.flow.smeo

import com.backbase.android.Backbase
import com.backbase.android.dbs.dataproviders.AssetsFileDBSDataProvider
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.address.AddressConfiguration
import com.backbase.android.flow.address.addressJourneyModule
import com.backbase.android.flow.address.models.AddressModel
import com.backbase.android.flow.address.usecase.AddressUseCase
import com.backbase.android.flow.address.usecase.AddressUseCaseDefaultImpl
import com.backbase.android.flow.businessrelations.BusinessRelationsConfiguration
import com.backbase.android.flow.businessrelations.BusinessRelationsJourneyModule
import com.backbase.android.flow.businessrelations.UserInfoProvider
import com.backbase.android.flow.businessrelations.model.UserInfo
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCase
import com.backbase.android.flow.businessrelations.usecase.BusinessRelationsUseCaseDefaultImpl
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.identityverification.DocScannerDataCenter
import com.backbase.android.flow.identityverification.IdentityVerificationConfiguration
import com.backbase.android.flow.identityverification.IdentityVerificationJourneyModule
import com.backbase.android.flow.identityverification.usecase.IdentityVerificationUseCase
import com.backbase.android.flow.identityverification.usecase.IdentityVerificationUseCaseDefaultImpl
import com.backbase.android.flow.otp.OtpConfiguration
import com.backbase.android.flow.otp.otpJourneyModule
import com.backbase.android.flow.otp.usecase.OtpUseCase
import com.backbase.android.flow.otp.usecase.OtpUseCaseDefaultImpl
import com.backbase.android.flow.productselector.*
import com.backbase.android.flow.smeo.Constants.Companion.INTERACTION_NAME
import com.backbase.android.flow.smeo.Constants.Companion.JUMIO_API_SECRET
import com.backbase.android.flow.smeo.Constants.Companion.JUMIO_API_TOKEN
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import com.backbase.android.flow.smeo.aboutyou.aboutYouJourneyModule
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCase
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCaseDefaultImpl
import com.backbase.android.flow.smeo.landing.landingConfiguration
import com.backbase.android.flow.smeo.walkthrough.walkthroughConfiguration
import com.backbase.android.flow.ssn.ssnConfiguration
import com.backbase.android.flow.ssn.ssnJourneyModule
import com.backbase.android.flow.ssn.usecase.SsnUsecase
import com.backbase.android.flow.ssn.usecase.SsnUsecaseDefaultImpl
import com.backbase.android.flow.uploadfiles.uploadFilesConfiguration
import com.backbase.android.flow.uploadfiles.uploadJourneyModule
import com.backbase.android.flow.uploadfiles.usecase.UploadFilesUseCase
import com.backbase.android.flow.uploadfiles.usecase.UploadFilesUseCaseImpl
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.deferredresources.DeferredText
import com.backbase.lookup.LocalStorage
import com.backbase.lookup.business_identity.businessIdentityConfiguration
import com.backbase.lookup.business_identity.businessIdentityJourneyModule
import com.backbase.lookup.business_identity.usecase.BusinessIdentityUseCase
import com.backbase.lookup.business_identity.usecase.BusinessIdentityUseCaseDefaultImpl
import com.backbase.lookup.business_info.businessInfoConfiguration
import com.backbase.lookup.business_info.businessInfoJourneyModule
import com.backbase.lookup.business_info.usecase.BusinessInfoUseCase
import com.backbase.lookup.business_info.usecase.BusinessInfoUseCaseDefaultImpl
import com.backbase.lookup.business_structure.BusinessStructureConfiguration
import com.backbase.lookup.business_structure.businessJourneyModule
import com.backbase.lookup.business_structure.businessStructureConfiguration
import com.backbase.lookup.business_structure.usecase.BusinessStructureUsecase
import com.backbase.lookup.business_structure.usecase.BusinessStructureUsecaseImpl
import com.backbase.lookup.business_structure.usecase.LookupUsecase
import com.backbase.lookup.business_structure.usecase.LookupUsecaseImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.lang.reflect.Type
import java.net.URI

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

    single<FlowClientContract>() {
        val interactionResponseDefaultBodyType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        com.backbase.android.flow.v2.FlowClient(
            context = get(),
            baseUri = URI("$baseUrl/api/sme-onboarding"),
            dataProvider = get<NetworkDBSDataProvider>(),
            interactionName = INTERACTION_NAME,
            interactionResponseDefaultBodyType = interactionResponseDefaultBodyType
        )
    }

    factory<AboutYouUseCase> {
        return@factory AboutYouUseCaseDefaultImpl(
            flowClient = get(),
            aboutYouConfiguration = get()
        )
    }

    factory {
        AboutYouConfiguration {
            isOffline = false
            actionInit = "sme-onboarding-init"
            actionAboutYouSubmit = "sme-onboarding-anchor-data"
        }
    }

    loadKoinModules(aboutYouJourneyModule)

    //region OTP
    factory {
        OtpConfiguration {
            requestActionName = "request-otp"
            verifyActionName = "verify-otp"
            availableOtpChannelsActionName = "available-otp-channels"
            verificationCodeMaxLength = Integer(6)
            fetchOtpEmailActionName = "fetch-otp-email"

        }
    }

    factory<OtpUseCase> {
        OtpUseCaseDefaultImpl(
            flowClient = get(),
            configuration = get()
        )
    }

    loadKoinModules(listOf(otpJourneyModule))
    //endregion OTP

    factory<AddressUseCase> {
        return@factory AddressUseCaseDefaultImpl(
            flowClient = get(),
            configuration = get()
        )
    }

    factory<com.backbase.lookup.address.usecase.LookupAddressUseCase> {
        return@factory com.backbase.lookup.address.usecase.LookupAddressUseCaseDefaultImpl(
            flowClient = get(),
            configurationLookup = get(),
            localAddressStorage = get()
        )
    }

    factory {
        val localAddressStorage: LocalStorage by inject()
        com.backbase.lookup.address.LookupAddressConfiguration {
            submitActionName = "submit-address"
            description = DeferredText.Resource(R.string.lookup_journey_label_we_need_to_know_you)
            prefillAddress = { localAddressStorage.getAddressModel() }
        }
    }

    loadKoinModules(listOf(addressJourneyModule))
    loadKoinModules(listOf(com.backbase.lookup.address.addressJourneyModule))
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
        return@factory UploadFilesUseCaseImpl(
            context = get(),
            flowClient = get(),
            configuration = get()
        )
    }

    loadKoinModules(listOf(uploadJourneyModule))

    //endregion upload documents

    //region SSN
    factory {
        ssnConfiguration{
            isOffline = false
            submitSsnActionName = "submit-ssn"
            landingActionName = "sme-onboarding-landing-data"
        }
    }

    factory<SsnUsecase> {
        return@factory SsnUsecaseDefaultImpl(
            context = get(),
            flowClient = get(),
            configuration = get()
        )
    }

    loadKoinModules(listOf(ssnJourneyModule))
    //endregion SSN

    //region business relations
    loadKoinModules(listOf(BusinessRelationsJourneyModule))
    factory {

        BusinessRelationsConfiguration {
            isOffline = false
            createCaseActionName = "check-business-relation-and-document-requests-conditions"
            submitRelationTypeActionName = "select-relation-type"
            updateOwnerActionName = "update-owner"
            updateCurrentUserOwnerActionName = "update-current-user-owner"
            updateCurrentUserControlPersonActionName = "update-control-person"
            deleteOwnerActionName = "delete-business-person"
            updateControlPersonActionName = "update-control-person"
            deleteControlPersonActionName = "delete-control-person"
            requestBusinessPersonsActionName = "get-business-persons"
            submitControlPersonActionName = "select-control-person"
            requestBusinessRolesActionName = "get-business-roles"
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
            enableCurrentUserEditing = true
        }
    }

    factory <BusinessRelationsUseCase>{
        return@factory BusinessRelationsUseCaseDefaultImpl(
            context = get(),
            flowClient = get(),
            businessRelationsConfiguration = get()
        )
    }
    //endregion business relations

    //region Product Selector
    factory {
        return@factory ProductSelectorConfiguration{
            imageBaseUrl = baseUrl
            createCaseAction = "sme-onboarding-check-case-exist"
            requestProductsAction = "get-product-list"
            submitProductAction = "select-products"
            hideHelperLink = false
            selectionType = SelectionType.SINGLE
        }
    }
    loadKoinModules(listOf(ProductSelectorJourneyModule))

    factory<ProductSelectorUseCase> {
        return@factory ProductSelectorUseCaseDefaultImpl(
            flowClient = get(),
            productSelectorConfiguration = get()
        )
    }
    //endregion Product Selector

    factory {
        businessStructureConfiguration {
            isOffline = false
            createCaseActionName = null
            requestBusinessStructureActionName = "requestBusinessStructureAction"
            submitBusinessStructureActionName = "business-structure"
            requestCompanyLookupActionName = "company-lookup"
            submitCompanyDetailsActionName = "company-details"
        }
    }

    factory {
        businessInfoConfiguration {
            isOffline = false
            submitBusinessDetailsActionName = "business-details"
        }
    }

    //region Backbase
    //Backbase manages its own singleton instance, so we don't need Koin to manage it too:
    factory { Backbase.getInstance() }

    factory {
        val backbase = get<Backbase>()
    }

    single {
        Gson()
    }

    factory {
        AssetsFileDBSDataProvider(androidContext())
    }

    //endregion

    loadKoinModules(businessJourneyModule)
    loadKoinModules(businessInfoJourneyModule)

    //region Address Validation Journey
    factory {
        AddressConfiguration {
            val localAddressStorage: LocalStorage by inject()
            submitActionName = "submit-address"
            description = DeferredText.Resource(R.string.lookup_journey_label_we_need_to_know_you)
            prefillAddress = AddressModel(
                localAddressStorage.getAddressModel().numberAndStreet,
                localAddressStorage.getAddressModel().apt,
                localAddressStorage.getAddressModel().city,
                localAddressStorage.getAddressModel().state,
                localAddressStorage.getAddressModel().zipCode,

            )
        }
    }

    loadKoinModules(listOf(addressJourneyModule))
    //endregion

    factory<BusinessStructureUsecase> {
        val context = androidContext()
        val config: BusinessStructureConfiguration = get()
        return@factory BusinessStructureUsecaseImpl(context, get(), config)
    }

    factory<LookupUsecase> {
        val context = androidContext()
        val config: BusinessStructureConfiguration = get()
        return@factory LookupUsecaseImpl(context, get(), config)
    }

    factory<BusinessInfoUseCase> {
        BusinessInfoUseCaseDefaultImpl(
            context = get(),
            flowClient = get(),
            configuration = get()
        )
    }

    //region Business Identity
    factory {
        businessIdentityConfiguration {
            isOffline = false
            submitBusinessIdentityActionName = "business-identity"
        }
    }

    factory<BusinessIdentityUseCase> {
        BusinessIdentityUseCaseDefaultImpl(
            context = get(),
            flowClient = get(),
            configuration = get()
        )
    }

    loadKoinModules(listOf(businessIdentityJourneyModule))
    //endregion business Identity

    factory {
        landingConfiguration {
            applicationCenterUrl =
                "$baseUrl/sme-onboarding-application-center#/application-center-init"
        }
    }

    //region IDV
    loadKoinModules(listOf(IdentityVerificationJourneyModule))

    factory {
        IdentityVerificationConfiguration {
            apiToken = JUMIO_API_TOKEN
            apiSecretKey = JUMIO_API_SECRET
            dataCenter = DocScannerDataCenter.EU
            initiationActionName = "identity-verification-initiation"
            verificationActionName = "identity-verification-result"
        }
    }

    factory<IdentityVerificationUseCase> {
        IdentityVerificationUseCaseDefaultImpl(
            flowClient = get(),
            configuration = get()
        )
    }
    //endregion IDV

    single {
        return@single LocalStorage(androidContext())
    }

    single {
        return@single StepInfoPublisher()
    }

}
