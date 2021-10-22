package com.backbase.android.flow.address.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.backbase.android.flow.address.AddressConfiguration
import com.backbase.android.flow.address.AddressRouter
import com.backbase.android.flow.address.R
import com.backbase.android.flow.address.models.AddressModel
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.extensions.mapValues
import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.model.ApplicantType
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.deferredresources.DeferredText
import kotlinx.android.synthetic.main.screen_address.*
import org.koin.android.ext.android.inject

const val JOURNEY_NAME_ADDRESS = "JOURNEY_NAME_ADDRESS"

class AddressScreen : SecureFragment(R.layout.screen_address) {

    private var validator: ButtonValidator? = null
    private var navController: NavController? = null
    private val viewModel: AddressViewModel by inject()
    private val router: AddressRouter by inject()
    private val configuration: AddressConfiguration by inject()
    private var mainApplicantAddress: AddressModel? = null
    private var submitPayload: AddressModel? = null
    private var isJointAccount = false
    private var coApplicantId = ""
    private val stepPublisher: StepInfoPublisher by inject()

    companion object{
        val JOURNEY_HEADER_INFO_DEFAULT = linkedMapOf(
            JourneyStepsAddress.ADDRESS.value.name to HeaderInfo(
                DeferredText.Resource(R.string.address_validation_screen_title_address),
                DeferredText.Resource(R.string.address_validation_screen_subtitle_address),
                JourneyStepsAddress.ADDRESS.value.allowBack
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = tryFindNavController(view)
        handleViewModelStates()
        initViews()
    }

    private fun fetchAddress() {
        viewModel.fetchAddress()
        progressBar.visibility = View.VISIBLE
    }

    private fun initViews() {
        isJointAccount =
            ApplicantType.getSelectedApplicantType(requireContext()) == ApplicantType.COAPPLICANT
        coApplicantId = ApplicantType.getCoApplicantId(requireContext())
        if (configuration.prefillAddress == null && configuration.fetchActionName.isNotEmpty()) {
            fetchAddress()
        }
        initViewsByApplicantTypes()
        initAddressForm()
    }

    private fun initViewsByApplicantTypes() {
        configuration.prefillAddress?.let {
            mainApplicantAddress = it
            txtMainApplicantAddress.text = it.toString()
            setUIForSameAddressFetchSuccess()
        } ?: run {
            if (isJointAccount && coApplicantId.isEmpty()) {
                containerSameAddress.visibility = View.VISIBLE
                containerAddressForm.visibility = View.GONE
            } else {
                containerSameAddress.visibility = View.GONE
                containerAddressForm.visibility = View.VISIBLE
            }
        }
        initUI()
    }

    private fun initAddressForm() {
        clearAddressForm()
        fillAddressform(submitPayload)
    }

    private fun fillStates(): Array<String> {
        val stateResources = requireContext().resources.getStringArray(R.array.states)
        txtState.fill(requireContext(), stateResources.toList())
        return stateResources
    }

    private fun clearAddressForm() {
        txtStreetNumber.setText("")
        txtApt.setText("")
        txtCity.setText("")
        txtState.setText("")
        txtZip.setText("")
    }

    private fun fillAddressform(address: AddressModel?) {
        address?.let {
            txtStreetNumber.setText(it.numberAndStreet)
            txtApt.setText(it.apt)
            txtCity.setText(it.city)
            txtState.setText(it.state)
            txtZip.setText(it.zipCode)
        }
        fillStates()
    }

    private fun initUI() {
        btnContinue.setOnClickListener {
            if (validator == null) {
                this.validator = initValidators()
            }

            if (btnContinue.isEnabled) {
                if (submitPayload == null) {
                    this.submitPayload = AddressModel(
                        txtStreetNumber.text.toString(),
                        txtApt.text.toString(),
                        txtCity.text.toString(),
                        txtState.text.toString(),
                        txtZip.text.toString()
                    )
                }

                this.submitPayload?.let {
                    viewModel.submitAddress(it)
                }
            }
        }

        chkIsSameAddress.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                containerMainApplicantAddress.visibility = View.VISIBLE
                containerAddressForm.visibility = View.GONE
                submitPayload = mainApplicantAddress
                fillAddressform(mainApplicantAddress)
            } else {
                submitPayload = null
                clearAddressForm()
                containerMainApplicantAddress.visibility = View.GONE
                containerAddressForm.visibility = View.VISIBLE
            }
        }
        configuration.description?.apply {
            lblHeader.text = resolve(requireContext())
            lblHeader.visibility = View.VISIBLE
        }

        val stateResources = fillStates()
        txtState.setText(stateResources[0])
    }

    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtStreetNumber.applyValidations(
                txtInputStreetNumber,
                ValidatorEmpty() to getString(R.string.address_validation_screen_street_required),
                ValidatorMaxChar(200) to getString(R.string.address_validation_screen_street_number_exceeded)
            ),
            txtApt.applyValidations(
                txtInputApt,
                ValidatorMaxChar(100) to getString(R.string.address_validation_screen_apt_exceeded)
            ),
            txtCity.applyValidations(
                txtInputCity,
                ValidatorEmpty() to getString(R.string.address_validation_screen_city_requried),
                ValidatorMaxChar(100) to getString(R.string.address_validation_screen_city_chars_exceeded)
            ),
            txtState.applyValidations(
                txtInputState,
                ValidatorEmpty() to getString(R.string.address_validation_screen_state_required)
            ),
            txtZip.applyValidations(
                txtInputZip,
                ValidatorMaxChar(5) to getString(R.string.address_validation_screen_zip_char_exceeded),
                ValidatorMinChar(5) to getString(R.string.address_validation_screen_zip_char_exceeded),
                ValidatorEmpty() to getString(R.string.address_validation_screen_zip_required)
            )
        )

    private fun handleViewModelStates() {
        val formViews = listOf(txtStreetNumber, txtApt, txtCity, txtState, txtZip)
        handleStates(
            viewModel.apiSubmitAddress.state,
            onSuccess = {
                router.onAddressFinished(it)
            },
            onFailed = { ex ->
                ex.message?.let {
                    hintContainer.showHintFailure(it)
                }
            },
            onBlockUI = {
                btnContinue.loading = true
                hintContainer.removeHint()
            },
            onUnblockUI = {
                btnContinue.loading = false
                hintContainer.removeHint()
            },
            formViews
        )

        handleStates(
            viewModel.apiFetchAddress.state,
            onSuccess = {
                it?.let { response ->
                    handleFetchResponse(response)
                } ?: run {
                    setUIForSameAddressFetchFail()
                }
            },
            onFailed = { ex ->
                setUIForSameAddressFetchFail()
            },
            onBlockUI = {
                hintContainer.removeHint()
            },
            onUnblockUI = {
                hintContainer.removeHint()
            },
            formViews
        )
    }

    private fun handleFetchResponse(response: InteractionResponse<OnboardingModel>) = try {
        response.body?.let { it ->
            progressBar.visibility = View.GONE
            it.address?.let {
                submitPayload = AddressModel(
                    numberAndStreet = it.numberAndStreet,
                    apt = it.apt,
                    city = it.city,
                    state = it.state,
                    zipCode = it.zipCode
                )
                mainApplicantAddress = submitPayload
            }

            fillAddressform(submitPayload)
            txtMainApplicantAddress.text = submitPayload.toString()
            if (isJointAccount && coApplicantId.isEmpty()) {
                setUIForSameAddressFetchSuccess()
            }
        }
    } catch (e: Exception) {
        setUIForSameAddressFetchFail()
    }

    private fun setUIForSameAddressFetchFail() {
        chkIsSameAddress.isEnabled = false
        containerMainApplicantAddress.visibility = View.GONE
        progressBar.visibility = View.GONE
        containerAddressForm.visibility = View.VISIBLE
    }

    private fun setUIForSameAddressFetchSuccess() {
        containerAddressForm.visibility = View.GONE
        progressBar.visibility = View.GONE
        chkIsSameAddress.isEnabled = true
        containerMainApplicantAddress.visibility = View.VISIBLE
        containerSameAddress.visibility = View.VISIBLE
    }

    override fun onResume() {
        setWindowFullScreen()
        stepPublisher.publish(JourneyStepsAddress.ADDRESS.value)
        super.onResume()
    }

    private fun setWindowFullScreen() {
        requireActivity().window.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    private fun tryFindNavController(view: View): NavController? {
        try {
            return view.findNavController()
        } catch (ex: Exception) {
        }
        return null
    }

}


enum class JourneyStepsAddress(val value: StepInfo) {
    ADDRESS(StepInfo(JOURNEY_NAME_ADDRESS, "ADDRESS", false))
}