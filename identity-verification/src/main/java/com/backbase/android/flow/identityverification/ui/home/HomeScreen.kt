package com.backbase.android.flow.identityverification.ui.home

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.identityverification.IdentityVerificationRouter
import com.backbase.android.flow.identityverification.IdentityVerificationViewModel
import com.backbase.android.flow.identityverification.R
import com.backbase.android.flow.identityverification.ui.scan.DocumentSelectionViewModel
import com.jumio.MobileSDK
import com.jumio.nv.custom.NetverifyCountry
import kotlinx.android.synthetic.main.screen_home.*
import org.koin.android.ext.android.inject

class HomeScreen : SecureFragment(R.layout.screen_home) {
    companion object {
        const val TAG = "IdentityVerificationHomeScreen"
        const val COUNTRIES_KEY = "countries"
        var requiredPermissionsRequestCode = 777
    }

    private var navController: NavController? = null
    private val identityVerificationViewModel: IdentityVerificationViewModel by inject()
    private val documentSelectionViewModel: DocumentSelectionViewModel by inject()
    private val router: IdentityVerificationRouter by inject()
    private var shouldMock: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlePermissions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnContinue.isEnabled = false
        identityVerificationViewModel.initializeIdentityVerification()
        handleViewModelStates()
        navController = tryFindNavController(view)
    }

    private fun initViews() {
        btnContinue.isEnabled = true
        initializeDocumentScanner()
        btnContinue.setOnClickListener {
            if (shouldMock) {
                mockScanDocument()
            } else {
                if (MobileSDK.hasAllRequiredPermissions(requireActivity())) {
                    documentSelectionViewModel.requestCountries(identityVerificationViewModel)
                } else {
                    handlePermissions()
                }
            }
        }
    }

    private fun initializeDocumentScanner() {
        when (identityVerificationViewModel.initDocumentScanner(requireActivity())) {
            IdentityVerificationViewModel.DocumentInitializeResult.INITIALIZED -> {
                btnContinue.isEnabled = true
                messageView.clearAllMessages()
            }
            IdentityVerificationViewModel.DocumentInitializeResult.NO_SCAN_SUPPORT -> {
                if (!shouldMock) {
                    btnContinue.isEnabled = false
                    messageView.showErrorMessage(
                        TAG,
                        getString(R.string.identity_verification_document_scan_device_not_supported_error)
                    )
                }
            }
        }
    }

    private fun mockScanDocument() {
        val choices = arrayOf("approved", "review", "fail")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("IDV mock result")
        builder.setItems(choices) { _, which ->
            identityVerificationViewModel.submitIdentityVerification(choices[which])
        }
        builder.show()
    }

    private fun handleViewModelStates() {
        handleSubmitViewModelStates()
        handleIdentityVerificationViewModelStates()
        handleDocumentSelectionViewModelStates()
    }

    private fun handleSubmitViewModelStates() {
        handleStates(identityVerificationViewModel.apiSubmitIdentityVerification.state,
            onSuccess = {
                router.onIdentityVerified(it)
            },
            onFailed = { ex ->
                ex.message?.let {
                    messageView.clearAllMessages()
                    messageView.showErrorMessage(
                        TAG, it
                    )
                }
            }
        )
    }

    private fun handleIdentityVerificationViewModelStates() {
        handleStates(identityVerificationViewModel.apiInitializeIdentityVerification.state,
            onSuccess = {
                it?.let { response ->
                    response.body?.get("url")?.let { value ->
                        shouldMock = value.contains("jumioMock.html")
                    }
                }
                initViews()
            },
            onFailed = { ex ->
                btnContinue.isEnabled = false
                ex.message?.let {
                    messageView.clearAllMessages()
                    messageView.showErrorMessage(TAG, it)
                }
            },
            onBlockUI = {
                btnContinue.loading = true
            },
            onUnblockUI = {
                btnContinue.loading = false
            }
        )
    }

    private fun handleDocumentSelectionViewModelStates() {
        handleStates(documentSelectionViewModel.state,
            onSuccess = { countries ->
                (countries as Map<String, NetverifyCountry>?)?.let {
                    val bundle = bundleOf(COUNTRIES_KEY to it)
                    navController?.navigate(R.id.action_to_countrySelectionScreen, bundle)
                }
            },
            onFailed = { ex ->
                ex.message?.let {
                    messageView.clearAllMessages()
                    messageView.showErrorMessage(TAG, it)
                }
            },
            onBlockUI = {
                btnContinue.loading = true
            },
            onUnblockUI = {
                btnContinue.loading = false
            }
        )
    }

    private fun handlePermissions() {
        if (!MobileSDK.hasAllRequiredPermissions(requireActivity())) {
            val permissionList = MobileSDK.getMissingPermissions(requireActivity())
            requestPermissions(permissionList, requiredPermissionsRequestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!(requestCode == requiredPermissionsRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(
                requireActivity(),
                getString(R.string.identity_verification_document_selection_permission_error),
                Toast.LENGTH_LONG
            ).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Navcontroller is null in the Unit test
    private fun tryFindNavController(view: View): NavController? {
        try {
            return view.findNavController()
        } catch (ex: Exception) {
        }
        return null
    }
}