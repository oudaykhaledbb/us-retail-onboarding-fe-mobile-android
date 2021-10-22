package com.backbase.lookup.address.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.model.StepConfiguration
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.validators.ButtonValidator
import com.backbase.android.flow.common.validators.ValidatorEmpty
import com.backbase.android.flow.common.validators.ValidatorMaxChar
import com.backbase.android.flow.common.validators.applyValidations
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.lookup.LookupJourney
import com.backbase.lookup.R
import com.backbase.lookup.ScreensLabel
import com.backbase.lookup.ScreensMonitor
import com.backbase.lookup.address.LookupAddressConfiguration
import com.backbase.lookup.address.models.AddressModel
import kotlinx.android.synthetic.main.screen_lookup_address.*
import kotlinx.android.synthetic.main.screen_lookup_address_apt.*
import kotlinx.android.synthetic.main.screen_lookup_address_city.*
import kotlinx.android.synthetic.main.screen_lookup_address_state.*
import kotlinx.android.synthetic.main.screen_lookup_address_street.*
import kotlinx.android.synthetic.main.screen_address_zip.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

class LookupAddressScreen : SecureFragment(R.layout.screen_lookup_address) {

    private var buttonValidator: ButtonValidator? = null
    private val viewModel: AddressViewModel by inject()
    private val screensMonitor: ScreensMonitor by inject()
    private val lookupAddressConfiguration: LookupAddressConfiguration by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onResume() {
        setWindowFullScreen()
        super.onResume()
        stepPublisher.publish(LookupJourney.JourneyStepsLookup.LOOKUP_ADDRESS.value)
        initViews()
    }

    private fun fillForm() {
        StepConfiguration.model?.address?.apply {
            txtStreetNumber.setText(numberAndStreet)
            txtApt.setText(apt)
            txtCity.setText(city)
            txtState.setText(state)
            txtZip.setText(zipCode)
        }
    }

    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtStreetNumber.applyValidations(
                txtInputStreetNumber,
                ValidatorEmpty() to getString(R.string.lookup_journey_street_required),
                ValidatorMaxChar(200) to getString(R.string.lookup_journey_street_number_exceeded)
            ),
            txtApt.applyValidations(
                txtInputApt,
                ValidatorMaxChar(100) to getString(R.string.lookup_journey_apt_exceeded)
            ),
            txtCity.applyValidations(
                txtInputCity,
                ValidatorEmpty() to getString(R.string.lookup_journey_city_requried),
                ValidatorMaxChar(100) to getString(R.string.lookup_journey_city_chars_exceeded)
            ),
            txtState.applyValidations(
                txtInputState,
                ValidatorEmpty() to getString(R.string.lookup_journey_state_required)
            ),
            txtZip.applyValidations(
                txtInputZip,
                ValidatorEmpty() to getString(R.string.lookup_journey_zip_required)
            )
        )


    private fun submit() {
        if (chkIsSameAddress.isChecked) {
            StepConfiguration.model?.address?.let {
                viewModel.submitAddress(
                    AddressModel(
                        it.numberAndStreet,
                        it.apt,
                        it.city,
                        it.state,
                        it.zipCode
                    )
                )
            }
        } else {
            viewModel.submitAddress(
                AddressModel(
                    txtStreetNumber.text.toString(),
                    txtApt.text.toString(),
                    txtCity.text.toString(),
                    txtState.text.toString(),
                    txtZip.text.toString()
                )
            )
        }
    }

    private fun initViews() {
        lookupAddressConfiguration.description?.apply {
            lblHeader.text = resolve(requireContext())
            lblHeader.visibility = View.VISIBLE
        }
        txtState.setText(requireContext().resources.getStringArray(R.array.states)[0])
        txtState.fill(
            requireContext(),
            requireContext().resources.getStringArray(R.array.states).toList()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillForm()
        initApis()
        initSameAddressCheckbox()
        btnContinue.setOnClickListener {
            if (buttonValidator == null) {
                this.buttonValidator = initValidators()
            }
            if (btnContinue.isEnabled) {
                submit()
            }
            txtState.fill(
                requireContext(),
                requireContext().resources.getStringArray(R.array.states).toList()
            )
        }
    }

    private fun initSameAddressCheckbox() {
        chkIsSameAddress.visibility =
            if (StepConfiguration.model?.address == null) View.GONE else View.VISIBLE
        chkIsSameAddress.setOnCheckedChangeListener { _, isChecked ->
            container.visibility = if (isChecked) View.GONE else View.VISIBLE
            if (!isChecked){
                buttonValidator = null
            }else{
                btnContinue.isEnabled = true
            }
        }
    }

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue,
            apiState = viewModel.apiSubmitAddress.state
        )
    }

    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<Any?>>
    ) {
        handleStates(
            viewModel.apiSubmitAddress.state,
            {
                screensMonitor.navigate(
                    findNavController(),
                    R.id.action_to_business_identity,
                    ScreensLabel.BusinessIdentityJourney
                )
            },
            { hintContainer.showHintFailure(it.message.toString()) },
            {
                hintContainer.removeHint()
                tappedButton.loading = true
            },
            { tappedButton.loading = false }
        )
    }

    private fun setWindowFullScreen() {
        requireActivity().window.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

}