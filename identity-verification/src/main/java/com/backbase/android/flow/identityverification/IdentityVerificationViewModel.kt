package com.backbase.android.flow.identityverification

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.viewmodel.ApiPublisher
import com.backbase.android.flow.identityverification.usecase.IdentityVerificationUseCase
import com.backbase.android.flow.v2.models.InteractionResponse
import com.jumio.core.data.document.ScanSide
import com.jumio.core.exceptions.PlatformNotSupportedException
import com.jumio.nv.NetverifyInitiateCallback
import com.jumio.nv.NetverifySDK
import com.jumio.nv.custom.*
import com.jumio.nv.data.document.NVDocumentType
import com.jumio.nv.data.document.NVDocumentVariant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class IdentityVerificationViewModel(
    private val configuration: IdentityVerificationConfiguration,
    private val useCase: IdentityVerificationUseCase
) : ViewModel(), NetverifyCustomSDKInterface, NetverifyInitiateCallback {

    internal val apiSubmitIdentityVerification = ApiPublisher<InteractionResponse<*>?>(this.viewModelScope)
    internal val apiInitializeIdentityVerification = ApiPublisher<InteractionResponse<Map<String,String>>?>(this.viewModelScope)

    // Scan State Region
    // Scan state is updated based on Netverify.
    @OptIn(ExperimentalCoroutinesApi::class)
    val scanState: ReceiveChannel<State<*>>
        get() = _scanBroadcastChannel.openSubscription()

    @ExperimentalCoroutinesApi
    private val _scanBroadcastChannel = BroadcastChannel<State<*>>(Channel.CONFLATED)

    private var scanCurrentState: State<*>? = null

    private fun notifyOnScanDocumentStateChange(state: State<*> = State.Idle) {
        viewModelScope.launch {
            scanCurrentState = state

            @OptIn(ExperimentalCoroutinesApi::class)
            _scanBroadcastChannel.send(state)
        }
    }
    //region end

    var netverifySDK: NetverifySDK? = null
    var customSDKController: NetverifyCustomSDKController? = null
    var requestCountriesListener: RequestCountriesListener? = null
    private var docScannerServiceType: DocScannerServiceType? = null
    var selectedScanSide: Int = 0
    var sides: List<ScanSide>? = null
    var selectedDocumentType: NVDocumentType? = null

    fun requestCountries(requestCountriesListener: RequestCountriesListener) {
        this.requestCountriesListener = requestCountriesListener
        docScannerServiceType = DocScannerServiceType.REQUEST_COUNTRIES
        customSDKController = netverifySDK?.start(this)
    }

    fun initializeIdentityVerification() {
        apiInitializeIdentityVerification.submit {
            useCase.initializeIdentityVerification()
        }
    }

    fun submitIdentityVerification(identityVerificationReference: String?) {
        apiSubmitIdentityVerification.submit {
            useCase.submitIdentityVerification(identityVerificationReference)
        }
    }

    fun initScanForPart(
        side: ScanSide?,
        scanView: NetverifyCustomScanView?,
        confirmationView: NetverifyCustomConfirmationView,
        customScanInterface: NetverifyCustomScanInterface?
    ): NetverifyCustomScanPresenter? {
        docScannerServiceType = DocScannerServiceType.REQUEST_SCAN
        return customSDKController?.initScanForPart(
            side,
            scanView,
            confirmationView,
            customScanInterface
        )
    }

    /**
     * Initializes standard NetverifySDK
     * Certain parameters can be adjusted individually (disabling paper documents entirely,
     * allowing only documents from specific countries, etc.)
     * To set a custom theme, use setCustomTheme(R.style.YOUR-CUSTOM-THEME-ID)
     */
    /**
     * Initializes standard NetverifySDK
     * Certain parameters can be adjusted individually (disabling paper documents entirely,
     * allowing only documents from specific countries, etc.)
     * To set a custom theme, use setCustomTheme(R.style.YOUR-CUSTOM-THEME-ID)
     */
    fun initDocumentScanner(activity: Activity): DocumentInitializeResult {
        try {

            if (!NetverifySDK.isSupportedPlatform(activity))
                return DocumentInitializeResult.NO_SCAN_SUPPORT

            if (NetverifySDK.isRooted(activity)) return DocumentInitializeResult.DEVICE_ROOTED

            netverifySDK = NetverifySDK.create(
                activity,
                configuration.apiToken,
                configuration.apiSecretKey,
                configuration.dataCenter
            )
            netverifySDK?.setEnableVerification(true)

            netverifySDK?.setPreselectedDocumentVariant(NVDocumentVariant.PLASTIC)
            netverifySDK?.setEnableIdentityVerification(true)
            netverifySDK?.initiate(this)
            return DocumentInitializeResult.INITIALIZED

        } catch (e: PlatformNotSupportedException) {
            netverifySDK = null
            return DocumentInitializeResult.NO_SCAN_SUPPORT
        }
    }

    override fun onNetverifyUserConsentRequried(p0: String?) {
        Log.d("IdentityVerification", "onNetverifyUserConsentRequried")
    }

    override fun onNetverifyCountriesReceived(p0: Map<String, NetverifyCountry>?, p1: String?) {
        requestCountriesListener?.onCountriesRetreivedSuccessfully(p0)
    }

    override fun onNetverifyResourcesLoaded(scanSides: MutableList<ScanSide>?) {
        sides = scanSides
    }

    override fun onNetverifyFinished(data: Bundle?) {
        data?.let { bundle ->
            val referenceId = bundle.get(NetverifySDK.EXTRA_SCAN_REFERENCE).toString()
            submitIdentityVerification(referenceId)
        }
    }

    override fun onNetverifyError(
        errorCode: String,
        errorMessage: String,
        retryPossible: Boolean,
        scanReference: String?,
        accountId: String?
    ) {
        when (docScannerServiceType) {
            DocScannerServiceType.REQUEST_COUNTRIES -> requestCountriesListener?.onCountriesFailed()
            DocScannerServiceType.REQUEST_SCAN -> onFailedToScanDocument(
                errorCode,
                errorMessage,
                scanReference
            )
        }
    }

    private fun onFailedToScanDocument(
        errorCode: String,
        errorMessage: String,
        scanReference: String?
    ) {
        notifyOnScanDocumentStateChange(
            State.Failed(
                FailedToScanException(
                    errorCode,
                    errorMessage,
                    scanReference
                )
            )
        )
        notifyOnScanDocumentStateChange(State.Idle)
    }

    fun setDocumentConfiguration(
        selectedCountry: NetverifyCountry?,
        documentType: NVDocumentType,
        documentVariant: NVDocumentVariant?
    ) {
        customSDKController?.setDocumentConfiguration(
            selectedCountry,
            documentType,
            documentVariant
        )
    }

    private enum class DocScannerServiceType { REQUEST_COUNTRIES, REQUEST_SCAN }
    enum class DocumentInitializeResult { INITIALIZED, NO_SCAN_SUPPORT, DEVICE_ROOTED }

    interface RequestCountriesListener {
        fun onCountriesFailed()
        fun onCountriesRetreivedSuccessfully(countries: Map<String, NetverifyCountry>?)
    }

    override fun onNetverifyInitiateError(p0: String?, p1: String?, p2: Boolean) {
        Log.d(p0.toString(), p1.toString())
    }

    override fun onNetverifyInitiateSuccess() {
        Log.d("Jumio", "Success")
    }
}

class FailedToScanException(
    val errorCode: String,
    val errorMessage: String,
    val scanReference: String?
) : Exception()