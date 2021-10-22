package com.backbase.android.flow.identityverification.ui.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.backbase.android.flow.common.fragment.SecureDialogFragment
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.identityverification.FailedToScanException
import com.backbase.android.flow.identityverification.IdentityVerificationRouter
import com.backbase.android.flow.identityverification.IdentityVerificationViewModel
import com.backbase.android.flow.identityverification.R
import com.jumio.core.data.document.ScanSide
import com.jumio.nv.custom.NetverifyCustomConfirmationView
import com.jumio.nv.custom.NetverifyCustomScanInterface
import com.jumio.nv.custom.NetverifyCustomScanPresenter
import com.jumio.nv.custom.NetverifyCustomScanView
import com.jumio.sdk.custom.SDKNotConfiguredException
import kotlinx.android.synthetic.main.screen_face_scan.*
import org.koin.android.ext.android.inject

class FaceScanScreen : SecureDialogFragment(), ScannerSDKContract {

    lateinit var scannerSDK: ScannerSDK
    private val identityVerificationViewModel: IdentityVerificationViewModel by inject()
    private val router: IdentityVerificationRouter by inject()
    private var scanSide: String? = null
    private var documentType: String? = null
    private var progressText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        return inflater.inflate(R.layout.screen_face_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        handleViewModelStates()
        customScanView.mode = NetverifyCustomScanView.MODE_FACE
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
            null,
            null,
            null,
            null,
            null,
            scanFaceHelpText
        )
        scannerSDK.initSDK()
    }

    private fun handleViewModelStates() {
        handleStates(identityVerificationViewModel.apiSubmitIdentityVerification.state,
            onSuccess = {
                router.onIdentityVerified(it)
            },
            onFailed = { ex ->
                router.onIdentityFailed(ex)
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
            Log.e("FaceScanScreen", "onScanFinished: ", e)
        }
        dismissAllowingStateLoss()
    }

    override fun onCancelOperation() {
        router.onIdentityFailed(Exception("onCancelOperation"))
    }

    override fun onCancelCurrentStep() {
        requireActivity().onBackPressed()
    }

    private fun failedToScanDocument(failedToScanException: FailedToScanException) {
        val jumioError =
            JumioErrorCode(failedToScanException.errorCode, failedToScanException.errorMessage)
        if (jumioError.recoveryStatus == RecoveryStatus.NON_RECOVERABLE) {
            router.onIdentityFailed(failedToScanException)
        } else {
            scannerSDK?.onRetryScan()
        }
    }

    override fun onStartScanningWithSide(
        side: ScanSide?,
        scanView: NetverifyCustomScanView?,
        confirmationView: NetverifyCustomConfirmationView,
        customScanInterface: NetverifyCustomScanInterface?
    ): NetverifyCustomScanPresenter? {
        return try {
            identityVerificationViewModel.initScanForPart(
                side,
                scanView,
                confirmationView,
                customScanInterface
            )
        } catch (e: SDKNotConfiguredException) {
            Log.e("FaceScanScreen", "onStartScanningWithSide: $e")
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
        hideView(
            customScanView,
            btnCapture
        )
        scanFaceHelpText?.text = HtmlCompat.fromHtml(
            getString(R.string.netverify_scanview_analyzing_biometrics),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        tvDocumentType.setText(R.string.netverify_scanview_title_check)
    }

    override fun dismissLoading() {

    }

    override fun getTheme(): Int {
        return com.backbase.android.flow.common.R.style.FullScreenDialog
    }

    override fun onResume() {
        super.onResume()
        scannerSDK.onResume()
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
        ): FaceScanScreen {
            val fragment = FaceScanScreen()
            val args = Bundle()
            args.putString(ARG_SCAN_SIDE, scanSide)
            args.putString(ARG_SCAN_DOCUMENT, document)
            args.putString(ARG_SCAN_PROGRESS, progressText)
            fragment.arguments = args
            return fragment
        }
    }
}