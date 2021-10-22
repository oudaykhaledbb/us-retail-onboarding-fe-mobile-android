package com.backbase.android.flow.identityverification

import android.os.Bundle
import androidx.test.espresso.matcher.ViewMatchers
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.identityverification.IdentityVerificationRouter
import com.backbase.android.flow.identityverification.IdentityVerificationViewModel
import com.backbase.android.flow.identityverification.SCOPE_ID
import com.backbase.android.flow.identityverification.core.journey.launchScreen
import com.backbase.android.flow.identityverification.core.journey.on
import com.backbase.android.flow.identityverification.core.koin.KoinTest
import com.backbase.android.flow.identityverification.core.view.checkMatches
import com.backbase.android.flow.identityverification.ui.scan.*
import com.backbase.android.flow.identityverification.usecase.IdentityVerificationUseCase
import com.backbase.android.flow.v2.models.InteractionResponse
import com.jumio.core.data.document.ScanSide
import kotlinx.android.synthetic.main.fragment_document_scan_screen.*
import org.hamcrest.core.IsNot.not
import org.junit.Test
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

class DocumentScanScreenTest : KoinTest() {

    override fun onKoinStarted() {
        provideBackbaseDependencies()
        loadKoinModules(IdentityVerificationJourneyModule)
        provideIdentityVerificationConfiguration()
        loadKoinModules(fragmentModule)
    }

    private val fragmentModule: Module by lazy {
        module {
            factory {
                object : IdentityVerificationRouter {
                    override fun onIdentityVerified(interactionResponse: InteractionResponse<*>?) {
                    }

                    override fun onIdentityFailed(data: Any?) {
                    }
                }
            }
        }
    }

    private val IdentityVerificationJourneyModule = module {

        single<IdentityVerificationUseCase> {
            return@single object : IdentityVerificationUseCase {
                override suspend fun initializeIdentityVerification(): InteractionResponse<OnboardingModel>? {
                    return null
                }

                override suspend fun submitIdentityVerification(verificationReference: String?): InteractionResponse<OnboardingModel>? {
                    return null
                }
            }
        }

        single {
            IdentityVerificationViewModel(get(), get())
        }

        viewModel {
            DocumentSelectionViewModel()
        }

        scope(named(SCOPE_ID)) { }
    }


    @Test
    fun testAllViewsAreDisplayed() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {
            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            customScanViewViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            clCaptureDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())

            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customLoadingIndicatorViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnRetakeViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun testShowLoader() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        val fragment = launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {
            fragment.onFragment { action ->
                action.showLoading()
            }

            customLoadingIndicatorViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            clCaptureDocumentViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customScanViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            //Non changed Views
            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnRetakeViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }


    @Test
    fun testDismissLoader() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        val fragment = launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {
            fragment.onFragment { action ->
                action.dismissLoading()
            }

            customLoadingIndicatorViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            clCaptureDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            customScanViewViewInteraction.checkMatches(ViewMatchers.isDisplayed())

            //Non changed Views
            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnRetakeViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }


    @Test
    fun testOnStartScanningWithSide() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        val fragment = launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {
            fragment.onFragment { action ->
                action.onStartScanningWithSide(
                    ScanSide.FACE,
                    action.customScanView,
                    action.customConfirmationView,
                    action.scannerSDK
                )
            }

            customLoadingIndicatorViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            clCaptureDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            customScanViewViewInteraction.checkMatches(ViewMatchers.isDisplayed())

            //Non changed Views
            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnRetakeViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }


    @Test
    fun testOnStartScanning() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        val fragment = launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {
            fragment.onFragment { action ->
                action.scannerSDK.netverifyCustomNfcInterface.onNetverifyNfcStarted()
            }

            customLoadingIndicatorViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            clCaptureDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            customScanViewViewInteraction.checkMatches(ViewMatchers.isDisplayed())

            //Non changed Views
            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnRetakeViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testOnScanningFinish() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        val fragment = launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {

            fragment.onFragment { action ->
                action.scannerSDK.netverifyCustomNfcInterface.onNetverifyNfcFinished()
            }


            customLoadingIndicatorViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            clCaptureDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            customScanViewViewInteraction.checkMatches(ViewMatchers.isDisplayed())

            //Non changed Views
            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnRetakeViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

    @Test
    fun testOnScanCanceled() {

        val args = Bundle()
        args.putString(ARG_SCAN_SIDE, "FRONT")
        args.putString(ARG_SCAN_DOCUMENT, "Identity Card")
        args.putString(ARG_SCAN_PROGRESS, "Step 1 of 3")
        val fragment = launchScreen<DocumentScanScreen>(args)

        on(DocumentScanScreenElements) {

            fragment.onFragment { action ->
                action.scannerSDK.scanningCanceled()
            }


            customLoadingIndicatorViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            clCaptureDocumentViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            customScanViewViewInteraction.checkMatches(ViewMatchers.isDisplayed())

            //Non changed Views
            imgFlashLightViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnDismissHelpViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnSkipNfcViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customConfirmationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            customAnimationViewViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            btnRetakeViewInteraction.checkMatches(ViewMatchers.isDisplayed())


            btnConfirmViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnCaptureViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))
            btnFallbackViewInteraction.checkMatches(not(ViewMatchers.isDisplayed()))

            tvDocumentTypeViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            imgBackViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvHelpViewInteraction.checkMatches(ViewMatchers.isDisplayed())
            tvStepsViewInteraction.checkMatches(ViewMatchers.isDisplayed())
        }
    }

}