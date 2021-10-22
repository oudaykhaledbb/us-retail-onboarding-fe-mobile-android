package com.backbase.android.flow.identityverification.ui.scan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.backbase.android.flow.common.fragment.SecureDialogFragment
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.identityverification.FailedToScanException
import com.backbase.android.flow.identityverification.IdentityVerificationRouter
import com.backbase.android.flow.identityverification.IdentityVerificationViewModel
import com.backbase.android.flow.identityverification.R
import com.backbase.android.flow.identityverification.ui.RetryDialog
import com.jumio.core.data.document.ScanSide
import com.jumio.nv.custom.NetverifyCustomConfirmationView
import com.jumio.nv.custom.NetverifyCustomScanInterface
import com.jumio.nv.custom.NetverifyCustomScanPresenter
import com.jumio.nv.custom.NetverifyCustomScanView
import com.jumio.sdk.custom.SDKNotConfiguredException
import kotlinx.android.synthetic.main.fragment_document_scan_screen.*
import kotlinx.android.synthetic.main.fragment_document_scan_screen.btnCapture
import kotlinx.android.synthetic.main.fragment_document_scan_screen.customAnimationView
import kotlinx.android.synthetic.main.fragment_document_scan_screen.customConfirmationView
import kotlinx.android.synthetic.main.fragment_document_scan_screen.customLoadingIndicator
import kotlinx.android.synthetic.main.fragment_document_scan_screen.customScanView
import kotlinx.android.synthetic.main.fragment_document_scan_screen.imgBack
import kotlinx.android.synthetic.main.fragment_document_scan_screen.imgFlashLight
import kotlinx.android.synthetic.main.fragment_document_scan_screen.tvDocumentType
import kotlinx.android.synthetic.main.fragment_document_scan_screen.tvSteps
import org.koin.android.ext.android.inject

class DocumentScanScreen : SecureDialogFragment(), ScannerSDKContract {

    lateinit var scannerSDK: ScannerSDK
    private val identityVerificationViewModel: IdentityVerificationViewModel by inject()
    private val router: IdentityVerificationRouter by inject()
    private var scanSide: String? = null
    private var documentType: String? = null
    private var progressText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().theme.applyStyle(R.style.IdentityVerificationJourney, false)
        if (arguments != null) {
            scanSide = requireArguments().getString(ARG_SCAN_SIDE)
            documentType =
                requireArguments().getString(ARG_SCAN_DOCUMENT)
            progressText =
                requireArguments().getString(ARG_SCAN_PROGRESS)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_document_scan_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewModelStates()
        imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        customScanView.mode = NetverifyCustomScanView.MODE_ID
        scannerSDK = ScannerSDK(
            requireActivity(),
            this,
            documentType,
            scanSide,
            progressText,
            imgBack,
            imgFlashLight,
            tvDocumentType,
            tvSteps,
            customScanView,
            customAnimationView,
            btnCapture,
            customConfirmationView,
            btnSkipNfc,
            btnConfirm,
            btnRetake,
            btnFallback,
            btnDismissHelp,
            tvHelp
        )
        scannerSDK.initSDK()
    }

    private fun handleViewModelStates() {
        handleStates(identityVerificationViewModel.apiSubmitIdentityVerification.state,
            onSuccess = {
                router.onIdentityVerified(it)
            },
            onBlockUI = {
                showLoading()
            },
            onUnblockUI = {
                dismissLoading()
            }
        )
        handleStates(identityVerificationViewModel.scanState,
            onSuccess = { },
            onFailed = { ex ->
                failedToScanDocument(ex as FailedToScanException)
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        identityVerificationViewModel.customSDKController?.consumeIntent(
            requestCode,
            resultCode,
            data
        )
    }

    private fun failedToScanDocument(failedToScanException: FailedToScanException) {
        val jumioError =
            JumioErrorCode(failedToScanException.errorCode, failedToScanException.errorMessage)
        if (jumioError.recoveryStatus == RecoveryStatus.RECOVERABLE) {
            showRetryDialog()
        } else {
            router.onIdentityFailed(failedToScanException)
        }
    }

    private fun showRetryDialog() {
        fragmentManager?.let {
            RetryDialog.getInstance()
                ?.setOnDoneListener {
                    btnRetake.performClick()
                }.show(it, "")
        }
    }

    override fun onScanForPartFinished() {
        identityVerificationViewModel.selectedScanSide++
        if (identityVerificationViewModel.selectedScanSide < identityVerificationViewModel.sides!!.size) {
            val progressString = requireContext().getString(
                R.string.netverify_helpview_progress_text,
                identityVerificationViewModel.selectedScanSide + 1,
                identityVerificationViewModel.sides?.size
            )
            val newScanFragment = getScanFragment(
                identityVerificationViewModel.sides!![identityVerificationViewModel.selectedScanSide].toString(),
                identityVerificationViewModel.selectedDocumentType?.getLocalizedName(requireContext()),
                progressString
            )
            newScanFragment.show(childFragmentManager, "dialog")
        }
    }

    override fun showSubmissionLoading() {

    }

    override fun onScanFinished() {
        try {
            identityVerificationViewModel.customSDKController?.finish()
        } catch (e: SDKNotConfiguredException) {
            Log.e("DocumentScanScreen", "onScanFinished: ", e)
        }
        dismissAllowingStateLoss()
    }

    override fun onCancelOperation() {
        router.onIdentityFailed(Exception("onCancelOperation"))
    }

    override fun onCancelCurrentStep() {
        requireActivity().onBackPressed()
    }

    override fun onStartScanningWithSide(
        side: ScanSide?,
        scanView: NetverifyCustomScanView?,
        confirmationView: NetverifyCustomConfirmationView,
        customScanInterface: NetverifyCustomScanInterface?
    ): NetverifyCustomScanPresenter? {
        return try {
            return identityVerificationViewModel.initScanForPart(
                side,
                scanView,
                confirmationView,
                customScanInterface
            )
        } catch (e: SDKNotConfiguredException) {
            Log.e("DocumentScanScreen", "onStartScanningWithSide: $e")
            null
        }
    }

    private fun hideView(vararg views: View?) {
        for (view in views) {
            if (view != null) {
                view.visibility = View.GONE
            }
        }
    }

    override fun showLoading() {
        customLoadingIndicator.visibility = View.VISIBLE
        hideView(
            customScanView,
            btnFallback,
            btnConfirm,
            btnRetake,
            btnCapture
        )
        tvHelp?.text = "Loading"
        tvDocumentType.setText(R.string.netverify_scanview_title_check)
    }

    override fun dismissLoading() {
        customLoadingIndicator.visibility = View.GONE
    }

    override fun getTheme(): Int {
        return com.backbase.android.flow.common.R.style.FullScreenDialog
    }

    override fun onResume() {
        scannerSDK.onResume()
        super.onResume()
    }

    override fun onPause() {
        scannerSDK.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        scannerSDK.onDestroy()
        super.onDestroy()
    }

    companion object {

        /**
         * Constructor with parameters
         *
         * @param scanSide     specifies which side of document is scanned (front, back or face in case of face scan)
         * @param document     specifies what kind of document is scanned (passport, DL, etc.)
         * @param progressText text tracking progress
         * @return fragment
         */
        fun newInstance(
            scanSide: String?,
            document: String?,
            progressText: String?
        ): DocumentScanScreen {
            val fragment = DocumentScanScreen()
            val args = Bundle()
            args.putString(ARG_SCAN_SIDE, scanSide)
            args.putString(ARG_SCAN_DOCUMENT, document)
            args.putString(ARG_SCAN_PROGRESS, progressText)
            fragment.arguments = args
            return fragment
        }
    }
}