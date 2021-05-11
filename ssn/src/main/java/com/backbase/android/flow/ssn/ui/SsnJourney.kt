package com.backbase.android.flow.ssn.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.ssn.R
import com.backbase.android.flow.ssn.SsnRouter
import kotlinx.android.synthetic.main.journey_ssn.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

class SsnJourney : Fragment(R.layout.journey_ssn) {

    private var buttonValidator: ButtonValidator? = null
    private val viewModel: SsnViewModel by inject()
    private val router: SsnRouter by inject()

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

    private fun submit() {
        viewModel.submitSsn(
            txtSsn.text.toString()
        )
    }

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue,
            apiState = viewModel.apiSubmitSsn.state
        )
    }

    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<Any?>>
    ) {
        handleStates(
            apiState,
            {
                router.onSsnFinished()
            },
            null,
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtSsn.applyValidations(
                txtInputSsn,
                ValidatorEmpty() to getString(R.string.field_required),
                ValidatorMaxChar(9) to getString(R.string.ssn_must_be_9_chars),
                ValidatorMinChar(9) to getString(R.string.ssn_must_be_9_chars),
            )
        )

}

