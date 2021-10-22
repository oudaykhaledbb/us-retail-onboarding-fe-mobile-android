package com.backbase.android.flow.identityverification.ui.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.identityverification.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jumio.analytics.JumioAnalytics
import com.jumio.analytics.MobileEvents
import com.jumio.analytics.Screen
import com.jumio.analytics.UserAction
import com.jumio.commons.utils.ScreenUtil
import com.jumio.core.data.document.ScanSide
import com.jumio.nv.custom.*
import com.jumio.nv.nfc.custom.NetverifyCustomNfcInterface
import com.jumio.nv.nfc.custom.NetverifyCustomNfcPresenter

class ScannerSDK(
    val activity: Activity,
    val contract: ScannerSDKContract,
    val documentType: String?,
    val scanSide: String?,
    val progressText: String?,
    val imgBack: View,
    val imgFlashLight: ImageView,
    val tvDocumentType: TextView,
    val tvSteps: TextView,
    val customScanView: NetverifyCustomScanView,
    val animationView: NetverifyCustomAnimationView,
    val btnCapture: Button,
    val confirmationView: NetverifyCustomConfirmationView,
    val btnSkipNfc: BackbaseButton?,
    val btnConfirm: BackbaseButton?,
    val btnRetake: TextView?,
    val btnFallback: BackbaseButton?,
    val btnDissmissHelp: BackbaseButton?,
    val tvHelp: TextView?

) : NetverifyCustomScanInterface {

    private var customNfcViewPresenter: NetverifyCustomNfcPresenter? = null
    private var customScanViewPresenter: NetverifyCustomScanPresenter? = null
    private var isOnConfirmation = false
    private var flashAvailableForCamera = false
    private val TAG = "NetverifyCustomScan"

    /**
     * Checks if device has flash available
     */
    private val isFlashAvailable: Boolean
        get() = customScanViewPresenter != null && !isOnConfirmation && customScanViewPresenter?.hasFlash() == true

    fun initSDK() {
        if (customScanViewPresenter == null) {
            customScanViewPresenter = contract.onStartScanningWithSide(
                ScanSide.valueOf(scanSide!!),
                customScanView,
                confirmationView,
                this
            )
        } else {
            customScanViewPresenter?.recreate(customScanView, confirmationView, this)
        }
        btnDissmissHelp?.setOnClickListener { dismissHelper() }
        btnConfirm?.setOnClickListener { confirm() }
        btnRetake?.setOnClickListener { onRetryScan() }
        btnCapture.setOnClickListener { customScanViewPresenter?.takePicture() }
        btnSkipNfc?.setOnClickListener { skipNfc() }
        imgFlashLight.setOnClickListener {
            customScanViewPresenter?.toggleFlash()
            setupFlashDrawable()
        }
        initFlashButton()
        tvSteps.text = progressText
        setHelpText(customScanViewPresenter?.helpText)
        customScanViewPresenter?.startScan()
    }

    fun setHelpText(helpText: String?) {
        if (!TextUtils.isEmpty(helpText) && tvHelp != null) {
            tvHelp?.text = helpText
        }
    }

    private fun initFlashButton() {
        imgFlashLight.visibility = if (isFlashAvailable) View.VISIBLE else View.GONE
    }

    fun onResume() {
        if (customScanViewPresenter != null) {
            customScanViewPresenter?.resume()
        }
    }

    fun onPause() {
        if (customScanViewPresenter != null) {
            customScanViewPresenter?.pause()
        }
    }

    fun onDestroy() {
        if (customScanViewPresenter != null) {
            customScanViewPresenter?.destroy()
        }
    }

    fun buildNfcSettingsDialog() {
        try {
            if (activity != null) {
                MaterialAlertDialogBuilder(activity)
                    .setTitle(com.jumio.nv.R.string.netverify_nfc_enable_dialog_title)
                    .setMessage(com.jumio.nv.R.string.netverify_nfc_enable_dialog_text)
                    .setPositiveButton(android.R.string.yes) { dialog, _ ->
                        dialog.dismiss()
                        activity?.startActivity(Intent("android.settings.NFC_SETTINGS"))
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ ->
                        dialog.dismiss()
                        JumioAnalytics.add(
                            MobileEvents.userAction(
                                JumioAnalytics.getSessionId(),
                                Screen.ERROR,
                                UserAction.CANCEL
                            )
                        )
                        customNfcViewPresenter?.cancel()
                    }
                    .show()
            }
        } catch (e: Exception) { //do not handle
            Log.e(TAG, "dialog builder: ", e)
        }
    }

    fun buildNfcErrorDialog(errorMessage: String?, retryable: Boolean) {
        try {
            if (activity != null) {
                val dialogBuilder = MaterialAlertDialogBuilder(activity)
                dialogBuilder.setTitle(R.string.netverify_nfc_general_error_dialog_title)
                dialogBuilder.setMessage(errorMessage)

                setHelpText(customScanViewPresenter?.helpText)
                hideView(false, animationView, btnRetake, tvHelp)
                if (retryable) { // retryable error
                    dialogBuilder.setPositiveButton(R.string.jumio_button_retry) { dialog, _ ->
                        dialog.dismiss()
                        customNfcViewPresenter?.retry()
                    }
                    dialogBuilder.setNegativeButton(R.string.jumio_button_cancel) { dialog, _ ->
                        dialog.dismiss()
                        customNfcViewPresenter?.cancel()
                    }
                } else { // not retryable error
                    dialogBuilder.setNeutralButton(R.string.jumio_button_cancel) { dialog, _ ->
                        dialog.dismiss()
                        customNfcViewPresenter?.cancel()
                    }
                }
                dialogBuilder.show()
            }
        } catch (e: Exception) { //do not handle
            Log.e(TAG, "dialog builder: ", e)
        }
    }

    override fun onNetverifyScanForPartFinished(canSide: ScanSide, allPartsScanned: Boolean) {
        customScanViewPresenter?.destroy()
        customScanViewPresenter = null
        // not all necessary parts scanned yet
        if (!allPartsScanned) { //index refers to list containing all possible sides
            contract.onScanForPartFinished()
        } else { //show loading on scan view during submission
            contract.showSubmissionLoading()
            contract.onScanFinished()
        }
    }

    override fun onNetverifyExtractionStarted() {
        Log.i(TAG, "onNetverifyExtractionStarted")
        imgFlashLight.visibility = if (isFlashAvailable) View.VISIBLE else View.GONE
        if (tvHelp?.visibility == View.GONE) {
            showView(tvHelp)
        }
        showLoading()
    }

    override fun onNetverifyPrepareScanning() {

    }

    fun showLoading() {
        hideView(
            true,
            customScanView,
            btnFallback,
            btnConfirm,
            btnRetake,
            btnCapture
        )
        tvDocumentType.setText(R.string.netverify_scanview_title_check)
    }

    private fun showView(vararg views: View?) {
        for (view in views) {
            if (view != null) {
                view.visibility = View.VISIBLE
            }
        }
    }

    private fun setupFlashDrawable() {
        imgFlashLight.setImageResource(if (customScanViewPresenter?.isFlashOn == true) R.drawable.ic_flash_off else R.drawable.ic_flash_on)
    }

    private fun hideView(showLoading: Boolean?, vararg views: View?) {
        for (view in views) {
            if (view != null) {
                view.visibility = View.GONE
            }
        }
        if (showLoading == true) {
            contract.showLoading()
        } else {
            contract.dismissLoading()
        }
    }

    private fun skipNfc() {
        hideView(false, animationView, btnSkipNfc)
        customNfcViewPresenter?.cancel()
        customNfcViewPresenter = null
        animationView.destroy()
        Log.i(TAG, "Skip NFC btn clicked")
    }

    private val isPortrait: Boolean
        get() {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y > size.x
        }

    private fun initScanView(customScanView: NetverifyCustomScanView?) { //Changes layout parameters if device is in portrait mode
        val isPortrait = isPortrait
        val params = ConstraintLayout.LayoutParams(
            if (isPortrait) ConstraintLayout.LayoutParams.MATCH_PARENT else ConstraintLayout.LayoutParams.WRAP_CONTENT,
            if (isPortrait) ConstraintLayout.LayoutParams.WRAP_CONTENT else ScreenUtil.dpToPx(
                activity.applicationContext,
                300
            )
        )

        setupFlashDrawable()

        customScanView?.layoutParams = params
        if (customScanViewPresenter != null) {
            if (isPortrait) {
                customScanView?.ratio =
                    if (customScanViewPresenter?.scanMode == NetverifyScanMode.FACE_IPROOV || customScanViewPresenter?.scanMode == NetverifyScanMode.FACE_MANUAL) 0.71f else 0.9f
            } else {
                customScanView?.ratio =
                    if (customScanViewPresenter?.scanMode == NetverifyScanMode.FACE_IPROOV || customScanViewPresenter?.scanMode == NetverifyScanMode.FACE_MANUAL) 1.66f else 1.0f
            }
        }
    }

    private fun confirm() {
        showSubmissionLoading()
        isOnConfirmation = false
        activity?.invalidateOptionsMenu()
        customScanViewPresenter?.confirmScan()
        Log.i(TAG, "Scan btn confirm clicked")
    }

    fun showSubmissionLoading() {
        hideView(
            true,
            confirmationView,
            customScanView,
            btnCapture
        )
    }

    private fun showShutterButton(visible: Boolean) {
        if (btnCapture != null) {
            btnCapture.visibility = if (visible) View.VISIBLE else View.GONE
        }
        if (tvHelp != null) {
            tvHelp?.visibility = if (visible) View.GONE else View.VISIBLE
        }
    }

    private fun setFallbackVisibility(visible: Boolean) {
        btnFallback?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun dismissHelper() {
        hideView(false, animationView, btnDissmissHelp)
        initScanView(customScanView)
        showView(
            tvDocumentType,
            tvSteps,
            customScanView
        )
        if (customScanViewPresenter?.isFallbackAvailable == true) {
            setFallbackVisibility(true)
        }
        showShutterButton(customScanViewPresenter?.showShutterButton() == true)
        Log.i(TAG, "Dismiss help btn clicked")
    }

    fun onRetryScan() {
        if (!customScanViewPresenter!!.isFallbackAvailable) {
            setFallbackVisibility(false)
        } else {
            setFallbackVisibility(true)
        }
        isOnConfirmation = false
        if (imgFlashLight != null) {
            imgFlashLight?.visibility = if (isFlashAvailable) View.VISIBLE else View.GONE
        }

        setHelpText(customScanViewPresenter?.helpText)
        customScanViewPresenter?.retryScan()
        hideView(
            false,
            confirmationView,
            btnRetake,
            btnConfirm
        )


        if (customScanViewPresenter?.showShutterButton() == true) {
            showView(btnCapture)
            hideView(false, btnFallback, tvHelp)
            setHelpText("   ")
        } else {
            hideView(false, btnCapture)
        }
        showView(customScanView)
        tvDocumentType.text =
            activity.getString(R.string.netverify_helpview_small_title_capture, documentType, "")
                ?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        contract.dismissLoading()
    }

    /**
     * No US Address has been found in the barcode. The scan preview will switch to frontside scanning if available.
     * Check for the changed scan mode and help text. Will only be called on a Fastfill scan.
     */
    override fun onNetverifyNoUSAddressFound() {
        Log.i(TAG, "onNetverifyNoUSAddressFound")
    }

    override fun onNetverifyPresentConfirmationView(confirmationType: NetverifyConfirmationType?) {
        isOnConfirmation = true
        Log.i(TAG, "onNetverifyPresentConfirmationView")
        if ((activity is AppCompatActivity)) {
            if ((activity as? AppCompatActivity)?.supportActionBar != null) {
                (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(true)
            }
        }
        showConfirmation(confirmationType == NetverifyConfirmationType.CHECK_DOCUMENT_SIDE)
    }

    override fun onNetverifyShowLegalAdvice(legalAdvice: String?) {
        val toast = Toast.makeText(activity?.applicationContext, legalAdvice, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        Log.i(TAG, "onNetverifyShowLegalAdvice: $legalAdvice")
    }

    override fun getNetverifyCustomNfcInterface(): NetverifyCustomNfcInterface {
        Log.i(TAG, "getNetverifyCustomNfcInterface")
        return NetverifyCustomNfcImpl()
    }

    override fun onNetverifyCameraAvailable() {
        Log.i(TAG, "onNetverifyCameraAvailable")
        flashAvailableForCamera = customScanViewPresenter?.hasFlash() == true
        customScanViewPresenter?.isFallbackAvailable

        imgFlashLight.visibility = if (isFlashAvailable) View.VISIBLE else View.GONE
    }

    /**
     * Handles cancelled scan
     * If document scan was cancelled, user is asked to try again, if face scan was cancelled
     * user is shown appropriate help animation
     */
    fun scanningCanceled() {
        showView(btnRetake)
        hideView(false)
    }

    /**
     * Shows confirmation if document scan was successful, lets user either accept scanned image
     * or retake it in case the result is not satisfactory
     */
    fun showConfirmation(faceOnBack: Boolean) {
        hideView(
            false,
            customScanView,
            btnFallback,
            btnCapture
        )
        showView(
            confirmationView,
            btnConfirm,
            btnRetake
        )
        if (tvHelp != null) {
            if (faceOnBack) {
                tvHelp?.text = "Back"
            } else {
                tvHelp?.text = "Confirm"
            }
        }
        tvDocumentType.setText(R.string.netverify_scanview_title_check)
    }

    override fun onNetverifyScanForPartCanceled(
        scanSide: ScanSide,
        netverifyCancelReason: NetverifyCancelReason
    ) {
        Log.i(TAG, "onNetverifyScanForPartCanceled")
        when (netverifyCancelReason) {
            NetverifyCancelReason.ERROR_GENERIC, NetverifyCancelReason.NOT_AVAILABLE -> {
                customScanViewPresenter?.stopScan()
                animationView?.let{
//                    customScanViewPresenter?.getHelpAnimation(it)
                }
                contract?.onCancelOperation()
            }
            NetverifyCancelReason.USER_BACK, NetverifyCancelReason.USER_CANCEL -> {
                contract?.onCancelCurrentStep()
            }

        }
    }

    override fun onNetverifyDisplayBlurHint() {
        Log.i(TAG, "onNetverifyDisplayBlurHint - Please Focus!")
    }

    override fun onNetverifyFaceInLandscape() {
        Log.i(TAG, "onNetverifyFaceInLandscape")
    }

    override fun onNetverifyStartNfcExtraction(netverifyCustomNfcPresenter: NetverifyCustomNfcPresenter) {
        Log.i(TAG, "onNetverifyStartNfcExtraction")

        customNfcViewPresenter = netverifyCustomNfcPresenter
        customNfcViewPresenter?.getHelpAnimation(animationView)

        tvDocumentType.text =
            activity.getString(R.string.netverify_nfc_header_start)
        hideView(false, tvSteps)

        showView(animationView, btnSkipNfc)
        hideView(
            false,
            customScanView,
            btnConfirm,
            btnRetake
        )
    }

    private inner class NetverifyCustomNfcImpl : NetverifyCustomNfcInterface {
        override fun onNetverifyNfcStarted() {
            tvDocumentType.text =
                activity.getString(R.string.netverify_nfc_header_extracting)
            Log.i(TAG, "onNetverifyNfcStarted")
        }

        override fun onNetverifyNfcUpdate(progress: Int) {
            Log.i(
                TAG,
                String.format("onNetverifyNfcUpdate %d", progress)
            )
        }

        override fun onNetverifyNfcFinished() {
            tvDocumentType.text =
                activity.getString(R.string.netverify_nfc_header_finish)
            hideView(false, animationView, btnSkipNfc)
            showView(tvSteps)
            Log.i(TAG, "onNetverifyNfcFinished")
        }

        override fun onNetverifyNfcSystemSettings() {
            buildNfcSettingsDialog()
            Log.i(TAG, "NFC not enabled")
        }

        override fun onNetverifyNfcError(errorMessage: String?, retryable: Boolean) {
            tvDocumentType.text =
                activity.getString(R.string.netverify_nfc_header_start)
            buildNfcErrorDialog(errorMessage, retryable)
            Log.e(
                TAG,
                String.format("$errorMessage, retry possible: $retryable")
            )
        }
    }
}

interface ScannerSDKContract {
    fun onScanForPartFinished()
    fun showSubmissionLoading()
    fun onScanFinished()
    fun showLoading()
    fun dismissLoading()
    fun onCancelOperation()
    fun onCancelCurrentStep()
    fun onStartScanningWithSide(
        side: ScanSide?,
        scanView: NetverifyCustomScanView?,
        confirmationView: NetverifyCustomConfirmationView,
        customScanInterface: NetverifyCustomScanInterface?
    ): NetverifyCustomScanPresenter?
}