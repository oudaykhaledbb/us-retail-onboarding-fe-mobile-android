package com.backbase.android.flow.smeo.business.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.validators.ValidatorCalendarNotEmpty
import com.backbase.android.flow.common.validators.ValidatorCharLength
import com.backbase.android.flow.common.validators.ValidatorEmpty
import com.backbase.android.flow.common.validators.applyValidations
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.smeo.business.ui.ButtonValidator
import com.backbase.android.flow.smeo.business.ui.InfoBottomSheet
import com.backbase.android.flow.smeo.business.ui.viewmodels.BusinessInfoViewModel
import com.google.android.material.datepicker.CalendarConstraints
import kotlinx.android.synthetic.main.screen_business_info.*
import kotlinx.android.synthetic.main.screen_business_info_date_established.*
import kotlinx.android.synthetic.main.screen_business_info_dba.*
import kotlinx.android.synthetic.main.screen_business_info_ein.*
import kotlinx.android.synthetic.main.screen_business_info_legal_name.*
import kotlinx.android.synthetic.main.screen_business_info_state_operating_in.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject


class BusinessInfoScreen : Fragment(R.layout.screen_business_info) {

    private val viewModel: BusinessInfoViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValidations()
        initContinueButton()
        initApis()
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue,
            apiState = viewModel.apiSubmitBusinessDetails.state
        )
    }

    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<Any?>>
    ) {
        handleStates(
            apiState,
            {
                findNavController().navigate(R.id.action_to_businessAddressScreen)
            },
            null,
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    private fun initContinueButton() {
        btnContinue.setOnClickListener {
            viewModel.submitBusinessDetails(
                legalName = txtLegalName.text.toString(),
                knownName = txtKnownName.text.toString(),
                ein = txtEin.text.toString().toIntOrNull(),
                establishedDate = calendarDateEstablished.dateFormat.toString(),
                operationState = txtStateOperatingIn.toString(),
            )
        }
    }

    private fun initValidations() {
        ButtonValidator(
                btnContinue,
                txtLegalName.applyValidations(
                        txtInputLegalName,
                        ValidatorEmpty() to getString(R.string.Legal_name_is_missing)
                ),
                calendarDateEstablished.applyValidations(txtCalendarHelperText,
                        ValidatorCalendarNotEmpty() to getString(R.string.date_established_is_missing),
                ),
            txtEin.applyValidations(
                txtInputEin,
                ValidatorCharLength(9) to getString(R.string.validation_ein_9_char)
            )
        )
    }


    private fun initViews() {
        txtInputKnownName.getChildAt(1).visibility = View.GONE
        btnWhenDoYouNeedEin.setOnClickListener { openEinLink() }
        txtStateOperatingIn.setText(requireContext().resources.getStringArray(R.array.states)[0])
        txtStateOperatingIn.fill(
            requireContext(),
            requireContext().resources.getStringArray(R.array.states).toList()
        )
        calendarDateEstablished.setValidator(object: CalendarConstraints.DateValidator{
            override fun writeToParcel(dest: Parcel?, flags: Int) {

            }
            override fun isValid(date: Long) = System.currentTimeMillis() > date
            override fun describeContents() = 0
        })
        btnMoreInfo.setOnClickListener { showBottomSheet() }
    }

    private fun openEinLink() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ein_link)))
        startActivity(browserIntent)
    }

    private fun showBottomSheet() {
        InfoBottomSheet.newInstance().show(childFragmentManager, "InfoBottomSheet")
    }

}