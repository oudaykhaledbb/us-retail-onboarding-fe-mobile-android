package com.backbase.android.flow.smeo.business.ui

import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.business.BusinessRouter
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.smeo.business.ui.validators.ValidatorCalendarNotEmpty
import com.backbase.android.flow.smeo.business.ui.validators.ValidatorEmpty
import com.backbase.android.flow.smeo.business.ui.validators.applyValidations
import com.backbase.android.flow.stepnavigation.StepNavigationView
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
    private val router: BusinessRouter by inject()
    private val header: StepNavigationView by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initValidations()
        initContinueButton()
        initApis()
    }

    override fun onResume() {
        super.onResume()
        header.setProgress(1)
            .setProgressText(getString(R.string.your_business))
            .setTitle(
                getString(
                    R.string.your_business_details
                )
            )
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
                router.onBusinessFinished()
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
            calendarDateEstablished.applyValidations(
                ValidatorCalendarNotEmpty() to getString(R.string.date_established_is_missing),
            )
        )
    }


    private fun initViews() {
        txtInputKnownName.getChildAt(1).visibility = View.GONE
        txtInputEin.getChildAt(1).visibility = View.GONE
        txtStateOperatingIn.fill(
            requireContext(),
            requireContext().resources.getStringArray(R.array.states).toList()
        )
        txtStateOperatingIn.setText(requireContext().resources.getStringArray(R.array.states)[0])
        calendarDateEstablished.setValidator(object: CalendarConstraints.DateValidator{
            override fun writeToParcel(dest: Parcel?, flags: Int) {

            }
            override fun isValid(date: Long) = System.currentTimeMillis() > date
            override fun describeContents() = 0
        })
    }

}