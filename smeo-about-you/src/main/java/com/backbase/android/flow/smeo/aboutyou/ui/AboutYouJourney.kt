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
import kotlinx.android.synthetic.main.journey_about_you.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject
import java.time.LocalDate

class AboutYouJourney : Fragment(R.layout.journey_about_you) {

    private var dateOfBirth: LocalDate? = null
    private val viewModel: AboutYouViewModel by inject()
    private val router: AboutYouRouter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApis()
        calendarDateOfBirth.addOnDateChangeListener {
            this.dateOfBirth = it
        }
        btnContinue.setOnClickListener {
            viewModel.submitAboutYou(
                txtFirstName.text.toString(),
                txtLastName.text.toString(),
                dateOfBirth.toString(),
                txtEmail.text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        initValidations()
    }

    private fun initValidations() {
        ButtonValidator(
            btnContinue,
            txtFirstName.applyValidations(
                txtInputFirstName,
                ValidatorEmpty() to "First name is missing"
            ),
            txtLastName.applyValidations(
                txtInputLastName,
                ValidatorEmpty() to "Last name is missing",
            ),
            calendarDateOfBirth.applyValidations(
                ValidatorCalendarNotEmpty() to "Date of birth is missing",
                ValidatorDateOfBirthOver18() to "Over 18"
            ),
            txtEmail.applyValidations(
                txtInputEmail,
                ValidatorEmpty() to "Email is missing",
                ValidatorEmail() to "Should be a valid email address.",
            )
        )
    }

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