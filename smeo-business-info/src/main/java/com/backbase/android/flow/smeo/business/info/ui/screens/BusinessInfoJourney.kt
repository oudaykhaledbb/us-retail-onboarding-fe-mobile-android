package com.backbase.android.flow.smeo.business.info.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.smeo.business.info.BusinessInfoRouter
import com.backbase.android.flow.smeo.business.info.ui.InfoBottomSheet
import com.backbase.android.flow.smeo.business.info.ui.viewmodels.BusinessInfoViewModel
import com.google.android.material.datepicker.CalendarConstraints
import kotlinx.android.synthetic.main.journey_business_info.*
import kotlinx.android.synthetic.main.screen_business_info_date_established.*
import kotlinx.android.synthetic.main.screen_business_info_dba.*
import kotlinx.android.synthetic.main.screen_business_info_ein.*
import kotlinx.android.synthetic.main.screen_business_info_legal_name.*
import kotlinx.android.synthetic.main.screen_business_info_state_operating_in.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class BusinessInfoJourney : Fragment(R.layout.journey_business_info) {

    private var buttonValidator: ButtonValidator? = null
    private val viewModel: BusinessInfoViewModel by inject()
    private val router: BusinessInfoRouter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApis()
        btnContinue.setOnClickListener {
            if (buttonValidator == null) {
                this.buttonValidator = initValidators()
            }
            if (btnContinue.isEnabled) {
                submit()
            }
        }
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
                router.onBusinessInfoFinished()
            },
            null,
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    private fun submit() {
        viewModel.submitBusinessDetails(
            legalName = txtKnownName.text.toString(),
            knownName = txtLegalName.text.toString(),
            ein = txtEin.text.toString().toIntOrNull(),
            establishedDate = manipulateEstablishedDate(
                calendarDateEstablished.text.toString(),
                calendarDateEstablished.dateFormat.toString()
            ),
            operationState = txtStateOperatingIn.text.toString(),
        )
    }

    fun manipulateEstablishedDate(originalDate: String, currentDateFormat: String): String {
        var formatter = SimpleDateFormat(currentDateFormat, Locale.ENGLISH)
        var date = formatter.parse(originalDate)
        val newSdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return "${newSdf.format(date)}T00:00:00.000Z"
    }

    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtLegalName.applyValidations(
                txtInputLegalName,
                ValidatorEmpty() to getString(R.string.Legal_name_is_missing)
            ),
            calendarDateEstablished.applyValidations(
                txtCalendarHelperText,
                ValidatorCalendarNotEmpty() to getString(R.string.date_established_is_missing),
            ),
            txtEin.applyValidations(
                txtInputEin,
                ValidatorCharLength(9) to getString(R.string.validation_ein_9_char)
            )
        )

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