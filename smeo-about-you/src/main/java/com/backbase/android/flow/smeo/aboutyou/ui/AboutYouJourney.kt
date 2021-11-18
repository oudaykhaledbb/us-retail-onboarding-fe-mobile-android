package com.backbase.android.flow.smeo.aboutyou.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
import com.backbase.android.flow.smeo.aboutyou.R
import com.backbase.deferredresources.DeferredText
import kotlinx.android.synthetic.main.journey_about_you.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject
import java.time.LocalDate

const val JOURNEY_NAME_ABOUT_YOU = "JOURNEY_NAME_ABOUT_YOU"

class AboutYouJourney : Fragment(R.layout.journey_about_you) {

    private var dateOfBirth: LocalDate? = null
    private var buttonValidator: ButtonValidator? = null
    private val viewModel: AboutYouViewModel by inject()
    private val router: AboutYouRouter by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsAboutYouJourney.ABOUT_YOU.value)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApis()
        calendarDateOfBirth.addOnDateChangeListener {
            this.dateOfBirth = it
        }
        btnContinue.setOnClickListener {
            hintContainer.removeHint()
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
                ValidatorEmpty() to getString(R.string.smeo_about_you_validation_first_name_missing)
            ),
            txtLastName.applyValidations(
                txtInputLastName,
                ValidatorEmpty() to getString(R.string.smeo_about_you_validation_last_name_missing),
            ),
            calendarDateOfBirth.applyValidations(txtCalendarHelperText,
                ValidatorCalendarNotEmpty() to getString(R.string.smeo_about_you_validation_dob_missing),
                ValidatorDateOfBirthOver18() to getString(R.string.smeo_about_you_validation_dob_between_18_99),
                ValidatorDateOfBirthLess99() to getString(R.string.smeo_about_you_validation_dob_between_18_99)
            ),
            txtEmail.applyValidations(
                txtInputEmail,
                ValidatorEmpty() to getString(R.string.smeo_about_you_validation_email_missing),
                ValidatorEmail() to getString(R.string.smeo_about_you_validation_email_format),
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
            {
                hintContainer.showHintFailure("${it.message}")
            },
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    companion object{
        val JOURNEY_HEADER_INFO_DEFAULT = linkedMapOf(
            JourneyStepsAboutYouJourney.ABOUT_YOU.value.name to HeaderInfo(
                DeferredText.Resource(R.string.smeo_about_you_title_about_you),
                DeferredText.Resource(R.string.smeo_about_you_subtitle_about_you),
                JourneyStepsAboutYouJourney.ABOUT_YOU.value.allowBack
            )
        )
    }

}

enum class JourneyStepsAboutYouJourney(val value: StepInfo) {
    ABOUT_YOU(StepInfo(JOURNEY_NAME_ABOUT_YOU, "About_YOU", false))
}


