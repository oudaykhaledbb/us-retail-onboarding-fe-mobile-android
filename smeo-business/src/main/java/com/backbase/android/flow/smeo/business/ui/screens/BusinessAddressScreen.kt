package com.backbase.android.flow.smeo.business.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.business.BusinessRouter
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.smeo.business.ui.viewmodels.BusinessAddressScreenViewModel
import kotlinx.android.synthetic.main.screen_business_address_apt.*
import kotlinx.android.synthetic.main.screen_business_address_city.*
import kotlinx.android.synthetic.main.screen_business_address_state.*
import kotlinx.android.synthetic.main.screen_business_address_street.*
import kotlinx.android.synthetic.main.screen_business_address_zip.*
import kotlinx.android.synthetic.main.screen_business_info.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

class BusinessAddressScreen : Fragment(R.layout.screen_business_address) {

    private val viewModel: BusinessAddressScreenViewModel by inject()
    private val router: BusinessRouter by inject()

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initViews() {
        txtState.setText(requireContext().resources.getStringArray(R.array.states)[0])
        txtState.fill(
                requireContext(),
                requireContext().resources.getStringArray(R.array.states).toList()
        )
        btnContinue.setOnClickListener {
            viewModel.submitBusinessAddress(
                    txtStreetNumber.text.toString(),
                    txtApt.text.toString(),
                    txtCity.text.toString(),
                    txtState.text.toString(),
                    txtZip.text.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApis()
    }

    private fun initApis() {
        handleStateForSubmitApis(
                tappedButton = btnContinue,
                apiState = viewModel.apiSubmitBusinessAddress.state
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

}