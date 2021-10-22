package com.backbase.android.flow.identityverification.ui.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.identityverification.IdentityVerificationViewModel
import com.backbase.android.flow.identityverification.R
import com.backbase.android.flow.identityverification.ui.home.HomeScreen.Companion.COUNTRIES_KEY
import com.jumio.MobileSDK
import com.jumio.nv.NetverifyDeallocationCallback
import com.jumio.nv.NetverifyDocumentData
import com.jumio.nv.NetverifySDK
import com.jumio.nv.custom.NetverifyCountry
import com.jumio.nv.data.document.NVDocumentType
import com.jumio.nv.data.document.NVDocumentVariant
import com.jumio.sdk.custom.SDKNotConfiguredException
import kotlinx.android.synthetic.main.screen_document_selection.*
import kotlinx.android.synthetic.main.view_country_selection.*
import org.koin.android.ext.android.inject

class DocumentSelectionScreen : SecureFragment(R.layout.screen_document_selection),
    NetverifyDeallocationCallback {

    private val identityVerificationViewModel: IdentityVerificationViewModel by inject()
    private var navController: NavController? = null
    private var selectedCountry: NetverifyCountry? = null
    private lateinit var netverifySDK: NetverifySDK
    private lateinit var countries: Map<String, NetverifyCountry>

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            countries = requireArguments().get(COUNTRIES_KEY) as Map<String, NetverifyCountry>
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = tryFindNavController(view)
        llCountry.setOnClickListener {
            if (MobileSDK.hasAllRequiredPermissions(requireActivity())) {
                chooseCountry(countries)
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.identity_verification_document_selection_permission_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun chooseCountry(countries: Map<String, NetverifyCountry>) {
        llCountry.isEnabled = true
        val fm: FragmentManager = (context as FragmentActivity).supportFragmentManager
        val dialogFragment = CountrySelectionDialogFragment()
            .setCountriesList(ArrayList(countries.values.sortedBy { it.countryName() }))
            .setTitle("Choose your country")
            .setOnRadioButtonSelectedListener { netVerifyCountry ->
                lblSelection.text = netVerifyCountry.countryName()
                onCountrySelection(netVerifyCountry)
            }
        dialogFragment.show(fm, "SelectCountryDialog")
    }

    fun onCountrySelection(netVerifyCountry: NetverifyCountry) {
        selectedCountry = netVerifyCountry
        fetchOptions(netVerifyCountry)
        netVerifyCountry.getDocumentVariants(NVDocumentType.VISA)
    }

    private fun fetchOptions(netVerifyCountry: NetverifyCountry) {
        val documentVariants = netVerifyCountry.documentTypes
        btnVisa.visibility =
            if (documentVariants.contains(NVDocumentType.VISA)) View.VISIBLE else View.GONE
        btnIdentificationCard.visibility =
            if (documentVariants.contains(NVDocumentType.IDENTITY_CARD)) View.VISIBLE else View.GONE
        btnDriverLicense.visibility =
            if (documentVariants.contains(NVDocumentType.DRIVER_LICENSE)) View.VISIBLE else View.GONE
        btnPassport.visibility =
            if (documentVariants.contains(NVDocumentType.PASSPORT)) View.VISIBLE else View.GONE

        btnVisa.setOnClickListener {
            scanVisa(
                NVDocumentType.VISA,
                netVerifyCountry.getDocumentVariants(NVDocumentType.VISA)
            )
        }
        btnIdentificationCard.setOnClickListener {
            scanID(
                NVDocumentType.IDENTITY_CARD,
                netVerifyCountry.getDocumentVariants(NVDocumentType.IDENTITY_CARD)
            )
        }
        btnDriverLicense.setOnClickListener {
            scanDriverLicense(
                NVDocumentType.DRIVER_LICENSE,
                netVerifyCountry.getDocumentVariants(NVDocumentType.DRIVER_LICENSE)
            )
        }
        btnPassport.setOnClickListener {
            scanPassport(
                NVDocumentType.PASSPORT,
                netVerifyCountry.getDocumentVariants(NVDocumentType.PASSPORT)
            )
        }

    }

    private fun scanDriverLicense(
        documentType: NVDocumentType,
        documentVariants: Set<NVDocumentVariant>
    ) {
        scan(documentType, documentVariants.first())
    }

    private fun scanVisa(
        documentType: NVDocumentType,
        documentVariants: Set<NVDocumentVariant>
    ) {
        scan(documentType, documentVariants.first())
    }

    private fun scanID(
        documentType: NVDocumentType,
        documentVariants: Set<NVDocumentVariant>
    ) {
        scan(documentType, documentVariants.first())
    }

    private fun scanPassport(
        documentType: NVDocumentType,
        documentVariants: Set<NVDocumentVariant>
    ) {
        scan(documentType, documentVariants.first())
    }

    override fun onResume() {
        try {
            identityVerificationViewModel.customSDKController?.resume()
        } catch (e: SDKNotConfiguredException) {
            Log.e(TAG, "onResume: ", e)
        }
        super.onResume()
    }

    override fun onDestroy() {
        try {
            if (identityVerificationViewModel.customSDKController != null) {
                identityVerificationViewModel.customSDKController?.destroy()
                identityVerificationViewModel.customSDKController = null
            }
        } catch (e: SDKNotConfiguredException) {

        }
        super.onDestroy()
    }

    private fun scan(
        documentType: NVDocumentType,
        documentVariant: NVDocumentVariant?
    ) {
        identityVerificationViewModel.setDocumentConfiguration(
            selectedCountry,
            documentType,
            documentVariant
        )
        identityVerificationViewModel.selectedScanSide = 0
        //Start scanning for side
        val progressString = requireActivity().getString(
            R.string.netverify_helpview_progress_text,
            identityVerificationViewModel.selectedScanSide + 1,
            identityVerificationViewModel.sides?.size
        )
        identityVerificationViewModel.selectedDocumentType = documentType
        val fragmnent = getScanFragment(
            identityVerificationViewModel.sides?.get(identityVerificationViewModel.selectedScanSide)
                .toString(),
            documentType?.getLocalizedName(requireContext()),
            progressString
        )
        fragmnent.show(childFragmentManager, "dialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NetverifySDK.REQUEST_CODE) {

            val scanReference = data?.getStringExtra(NetverifySDK.EXTRA_SCAN_REFERENCE)

            if (resultCode == Activity.RESULT_OK) {
                //Handle the success case and retrieve scan data
                val documentData =
                    data?.getParcelableExtra<Parcelable>(NetverifySDK.EXTRA_SCAN_DATA) as? NetverifyDocumentData
                val mrzData = documentData?.mrzData
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Handle the error cases as highlighted in our documentation: https://github.com/Jumio/mobile-sdk-android/blob/master/docs/integration_faq.md#managing-errors
                val errorMessage = data?.getStringExtra(NetverifySDK.EXTRA_ERROR_MESSAGE)
                val errorCode = data?.getStringExtra(NetverifySDK.EXTRA_ERROR_CODE)
            }
            //At this point, the SDK is not needed anymore. It is highly advisable to call destroy(), so that
            //internal resources can be freed.
            netverifySDK.destroy()
            netverifySDK.checkDeallocation(this)
        }
    }

    override fun onNetverifyDeallocated() {

    }

    // Navcontroller is null in the Unit test
    private fun tryFindNavController(view: View): NavController? {
        try {
            return view.findNavController()
        } catch (ex: Exception) {
        }
        return null
    }

    companion object {
        private const val TAG = "JumioSDK_Netverify"
    }
}