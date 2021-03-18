package com.backbase.android.flow.smeo.business.ui

import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.smeo.business.ui.validators.ValidatorCalendarNotEmpty
import com.backbase.android.flow.smeo.business.ui.validators.ValidatorEmpty
import com.backbase.android.flow.smeo.business.ui.validators.applyValidations
import com.google.android.material.datepicker.CalendarConstraints
import kotlinx.android.synthetic.main.screen_business_info_date_established.*
import kotlinx.android.synthetic.main.screen_business_info_dba.*
import kotlinx.android.synthetic.main.screen_business_info_ein.*
import kotlinx.android.synthetic.main.screen_business_info_legal_name.*
import kotlinx.android.synthetic.main.screen_business_info_state_operating_in.*
import org.koin.android.ext.android.inject

class BusinessInfoScreen : Fragment(R.layout.screen_business_info) {

    private val viewModel: BusinessInfoViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initValidations()
    }

    private fun initValidations() {
        txtLegalName.applyValidations(
            txtInputLegalName,
            ValidatorEmpty() to getString(R.string.Legal_name_is_missing)
        )
        calendarDateEstablished.applyValidations(
            ValidatorCalendarNotEmpty() to getString(R.string.date_established_is_missing),
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