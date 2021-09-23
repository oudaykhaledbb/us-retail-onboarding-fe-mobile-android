package com.backbase.android.flow.smeo.aboutyou.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
import com.backbase.android.flow.smeo.aboutyou.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.journey_about_you.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject
import java.time.LocalDate

class AboutYouJourney : Fragment(R.layout.journey_about_you) {

    private var dateOfBirth: LocalDate? = null
    private var buttonValidator: ButtonValidator? = null
    private val viewModel: AboutYouViewModel by inject()
    private val router: AboutYouRouter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApis()
        calendarDateOfBirth.addOnDateChangeListener {
            this.dateOfBirth = it
        }
        btnContinue.setOnClickListener {
            if (buttonValidator == null) {
                this.buttonValidator = initValidators()
            }
            if (btnContinue.isEnabled) {
                submit()
            }
        }
    }

    private fun submit(){
        viewModel.submitAboutYou(
            txtFirstName.text.toString(),
            txtLastName.text.toString(),
            dateOfBirth.toString(),
            txtEmail.text.toString()
        )
    }

    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtFirstName.applyValidations(
                txtInputFirstName,
                ValidatorEmpty() to getString(R.string.validation_first_name_missing)
            ),
            txtLastName.applyValidations(
                txtInputLastName,
                ValidatorEmpty() to getString(R.string.validation_last_name_missing),
            ),
            calendarDateOfBirth.applyValidations(txtCalendarHelperText,
                ValidatorCalendarNotEmpty() to getString(R.string.validation_dob_missing),
                ValidatorDateOfBirthOver18() to getString(R.string.validation_dob_between_18_99),
                ValidatorDateOfBirthLess99() to getString(R.string.validation_dob_between_18_99)
            ),
            txtEmail.applyValidations(
                txtInputEmail,
                ValidatorEmpty() to getString(R.string.validation_email_missing),
                ValidatorEmail() to getString(R.string.validation_email_format),
            )
        )

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue,
            apiState = viewModel.apiSubmitAboutYou.state
        )
    }

    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<Any?>>
    ) {
        handleStates(
            apiState,
            {
                router.onAboutYouFinished()
            },
            null,
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

}