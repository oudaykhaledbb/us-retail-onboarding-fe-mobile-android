//package com.backbase.android.flow.smeo.business.ui.screens
//
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.backbase.android.design.button.BackbaseButton
//import com.backbase.android.flow.common.extensions.fill
//import com.backbase.android.flow.common.state.State
//import com.backbase.android.flow.common.validators.ValidatorEmpty
//import com.backbase.android.flow.common.validators.ValidatorMaxChar
//import com.backbase.android.flow.common.validators.applyValidations
//import com.backbase.android.flow.common.viewmodel.handleStates
//import com.backbase.android.flow.smeo.business.BusinessRouter
//import com.backbase.android.flow.smeo.business.R
//import com.backbase.android.flow.smeo.business.ui.ButtonValidator
//import com.backbase.android.flow.smeo.business.ui.viewmodels.BusinessAddressScreenViewModel
//import kotlinx.android.synthetic.main.screen_business_address_apt.*
//import kotlinx.android.synthetic.main.screen_business_address_city.*
//import kotlinx.android.synthetic.main.screen_business_address_state.*
//import kotlinx.android.synthetic.main.screen_business_address_street.*
//import kotlinx.android.synthetic.main.screen_business_address_zip.*
//import kotlinx.android.synthetic.main.screen_business_info.*
//import kotlinx.coroutines.channels.ReceiveChannel
//import org.koin.android.ext.android.inject
//
//class BusinessAddressScreen : Fragment(R.layout.screen_business_address) {
//
//    private var buttonValidator: ButtonValidator? = null
//    private val viewModel: BusinessAddressScreenViewModel by inject()
//    private val router: BusinessRouter by inject()
//
//    override fun onResume() {
//        super.onResume()
//        initViews()
//        btnContinue.setOnClickListener {
//            if (buttonValidator == null) {
//                this.buttonValidator = initValidators()
//            }
//            if (btnContinue.isEnabled) {
//                submit()
//            }
//            txtState.fill(
//                requireContext(),
//                requireContext().resources.getStringArray(R.array.states).toList()
//            )
//        }
//    }
//
//    private fun initValidators() =
//        ButtonValidator(
//            btnContinue,
//            txtStreetNumber.applyValidations(
//                txtInputStreetNumber,
//                ValidatorEmpty() to "Street number and name is required",
//                ValidatorMaxChar(200) to "Street number and name may not exceed 200 characters."
//            ),
//            txtApt.applyValidations(
//                txtInputApt,
//                ValidatorMaxChar(100) to "Apt / Suite may not exceed 100 characters."
//            ),
//            txtCity.applyValidations(
//                txtInputCity,
//                ValidatorEmpty() to "City is required",
//                ValidatorMaxChar(100) to "City may not exceed 100 characters."
//            ),
//            txtState.applyValidations(
//                txtInputState,
//                ValidatorEmpty() to "State is required"
//            ),
//            txtZip.applyValidations(
//                txtInputZip,
//                ValidatorEmpty() to "Zip code is required"
//            )
//        )
//
//
//    private fun submit() {
//        viewModel.submitBusinessAddress(
//            txtStreetNumber.text.toString(),
//            txtApt.text.toString(),
//            txtCity.text.toString(),
//            txtState.text.toString(),
//            txtZip.text.toString()
//        )
//    }
//
//    private fun initViews() {
//        txtState.setText(requireContext().resources.getStringArray(R.array.states)[0])
//        txtState.fill(
//            requireContext(),
//            requireContext().resources.getStringArray(R.array.states).toList()
//        )
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initApis()
//    }
//
//    private fun initApis() {
//        handleStateForSubmitApis(
//                tappedButton = btnContinue,
//                apiState = viewModel.apiSubmitBusinessAddress.state
//        )
//    }
//
//    private fun handleStateForSubmitApis(
//            tappedButton: BackbaseButton,
//            apiState: ReceiveChannel<State<Any?>>
//    ) {
//        handleStates(
//                apiState,
//                {
//                    findNavController().navigate(R.id.action_to_businessIdentityScreen)
//                },
//                null,
//                { tappedButton.loading = true },
//                { tappedButton.loading = false }
//        )
//    }
//
//}