package com.backbase.android.flow.smeo

import android.content.Context
import android.os.Bundle
import com.backbase.android.flow.address.ui.AddressScreen
import com.backbase.android.flow.address.ui.JOURNEY_NAME_ADDRESS
import com.backbase.android.flow.businessrelations.ui.screen.BusinessRelationsJourneyScreen
import com.backbase.android.flow.businessrelations.ui.screen.JOURNEY_NAME_BUSINESS_RELATIONS
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoObserver
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.identityverification.IdentityVerificationScreen
import com.backbase.android.flow.identityverification.JOURNEY_NAME_IDENTITY_VERIFICATION
import com.backbase.android.flow.otp.ui.JOURNEY_NAME_OTP
import com.backbase.android.flow.otp.ui.JourneyStepsOTP
import com.backbase.android.flow.productselector.screen.JOURNEY_NAME_PRODUCT_SELECTION
import com.backbase.android.flow.productselector.screen.ProductSelectionScreen
import com.backbase.android.flow.smeo.aboutyou.ui.AboutYouJourney
import com.backbase.android.flow.smeo.aboutyou.ui.JOURNEY_NAME_ABOUT_YOU
import com.backbase.android.flow.smeo.common.AppActivity
import com.backbase.android.flow.smeo.common.JourneysHeaderInfo
import com.backbase.android.flow.ssn.ui.JOURNEY_NAME_SSN
import com.backbase.android.flow.ssn.ui.SsnJourney
import com.backbase.android.flow.stepnavigation.HeaderDataProvider
import com.backbase.android.flow.uploadfiles.ui.JOURNEY_NAME_DOCUMENT_REQUEST
import com.backbase.android.flow.uploadfiles.ui.UploadFilesJourney
import com.backbase.deferredresources.DeferredText
import com.backbase.lookup.JOURNEY_NAME_LOOKUP_JOURNEY
import com.backbase.lookup.LookupJourney
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.koin.android.ext.android.inject
import org.koin.dsl.module

class MainActivity : AppActivity(R.layout.activity_main), StepInfoObserver {

    private val stepInfoPublisher: StepInfoPublisher by inject()
    var counter = 0
    private var isBackAllowed = false;

    val otpCustomHeaderDescription = linkedMapOf(
        JourneyStepsOTP.OTP.value.name to HeaderInfo(
            DeferredText.Resource(R.string.otp_header_title),
            DeferredText.Resource(R.string.otp_verification_screen_subtitle),
            JourneyStepsOTP.OTP.value.allowBack
        )
    )

    private val headerInfo = JourneysHeaderInfo(
        linkedMapOf(
            JOURNEY_NAME_ABOUT_YOU to AboutYouJourney.JOURNEY_HEADER_INFO_DEFAULT,
            JOURNEY_NAME_OTP to otpCustomHeaderDescription,
            JOURNEY_NAME_PRODUCT_SELECTION to ProductSelectionScreen.JOURNEY_HEADER_INFO_DEFAULT,
            JOURNEY_NAME_LOOKUP_JOURNEY to LookupJourney.HEADER_INFO_DEFAULT,
            JOURNEY_NAME_BUSINESS_RELATIONS to BusinessRelationsJourneyScreen.JOURNEY_HEADER_INFO_DEFAULT,
            JOURNEY_NAME_DOCUMENT_REQUEST to UploadFilesJourney.JOURNEY_HEADER_INFO_DEFAULT,
            JOURNEY_NAME_IDENTITY_VERIFICATION to IdentityVerificationScreen.JOURNEY_HEADER_INFO_DEFAULT,
            JOURNEY_NAME_ADDRESS to AddressScreen.JOURNEY_HEADER_INFO_DEFAULT,
            JOURNEY_NAME_SSN to SsnJourney.JOURNEY_HEADER_INFO_DEFAULT,
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepInfoPublisher.register(this)
    }

    fun getNumberOfSteps() =
        headerInfo.headerInfo.map { keyValue -> keyValue.value.size }.sumOf { it }

    fun getCurrentStep(stepInfo: StepInfo): Int? {
        return counter
    }

    override fun instantiateActivityModule() = module {
        val navController = findNavController()

        factory {
            aboutYouRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            otpRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }


        factory {
            productSelection(navController) {
                setTheme(R.style.AppTheme)
            }
        }


        factory {
            businessRelations(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            lookupRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            addressRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            idvRouter(navController) {
                setTheme(R.style.AppTheme)
            }
        }

        factory {
            uploadFilesRouter(navController)
        }

        factory {
            ssnRouter(this@MainActivity)
        }

        factory { return@factory header }

        factory {
            val context: Context by inject()
            return@factory HeaderDataProvider(mapOf(), context).setStepNavigationView(header)
        }

    }

    override fun onStepChange(stepInfo: StepInfo) {
        isBackAllowed = stepInfo.allowBack
        val screenInfo = headerInfo.headerInfo[stepInfo.journeyName]?.get(stepInfo.name)
        header.setTotalNumberOfSteps(getNumberOfSteps())
        header.setTitle(screenInfo?.title?.resolve(this).toString())
        header.setProgressText(screenInfo?.subTitle?.resolve(this).toString())
        getCurrentStep(stepInfo)?.let {
            header.setProgress(it + 1)
        }
        counter++
    }

    override fun onBackPressed() {
        if (isBackAllowed) {
            super.onBackPressed()
        }
    }
}