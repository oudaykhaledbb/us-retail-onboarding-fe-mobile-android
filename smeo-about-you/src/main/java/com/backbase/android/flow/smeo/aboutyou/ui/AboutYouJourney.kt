package com.backbase.android.flow.smeo.aboutyou.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.aboutyou.AboutYouRouter
import com.backbase.android.flow.smeo.aboutyou.R
import com.backbase.android.flow.stepnavigation.StepNavigationView
import kotlinx.android.synthetic.main.journey_about_you.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject
import java.time.LocalDate

class AboutYouJourney : Fragment(R.layout.journey_about_you) {

    private var dateOfBirth: LocalDate? = null
    private val viewModel: AboutYouViewModel by inject()
    private val router: AboutYouRouter by inject()
    private val header: StepNavigationView by inject()

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
        header.setProgress(1)
            .setProgressText(getString(R.string.personal_details))
            .setTitle(
                getString(
                    R.string.nice_to_meet_you
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