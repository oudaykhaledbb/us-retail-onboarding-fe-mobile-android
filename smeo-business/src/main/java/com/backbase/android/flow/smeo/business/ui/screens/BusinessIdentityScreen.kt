package com.backbase.android.flow.smeo.business.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.validators.ValidatorEmpty
import com.backbase.android.flow.common.validators.applyValidations
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.smeo.business.BusinessRouter
import com.backbase.android.flow.smeo.business.R
import com.backbase.android.flow.smeo.business.ui.ButtonValidator
import com.backbase.android.flow.smeo.business.ui.viewmodels.BusinessIdentityViewModel
import kotlinx.android.synthetic.main.screen_business_identity_business_description.*
import kotlinx.android.synthetic.main.screen_business_identity_company_website.*
import kotlinx.android.synthetic.main.screen_business_identity_industry.*
import kotlinx.android.synthetic.main.screen_business_info.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

class BusinessIdentityScreen : Fragment(R.layout.screen_business_identity) {

    private var buttonValidator: ButtonValidator? = null
    private val viewModel: BusinessIdentityViewModel by inject()
    private val router: BusinessRouter by inject()

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
            txtIndustry.fill(
                requireContext(),
                requireContext().resources.getStringArray(R.array.industries).toList()
            )
        }
    }

    private fun initViews() {
        txtIndustry.setText(requireContext().resources.getStringArray(R.array.industries)[0])
        txtIndustry.fill(
            requireContext(),
            requireContext().resources.getStringArray(R.array.industries).toList()
        )
    }

    private fun submit() {
        viewModel.submitBusinessIdentity(
            txtIndustry.text.toString(),
            txtBusinessDescription.text.toString(),
            txtCompanyWebsite.text.toString()
        )
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue,
            apiState = viewModel.apiSubmitBusinessIdentity.state
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


    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtIndustry.applyValidations(
                txtInputIndustry,
                ValidatorEmpty() to getString(R.string.industry_is_missing)
            ),
            txtBusinessDescription.applyValidations(
                txtInputBusinessDescription,
                ValidatorEmpty() to getString(R.string.business_description__is_missing),
            )
        )

}