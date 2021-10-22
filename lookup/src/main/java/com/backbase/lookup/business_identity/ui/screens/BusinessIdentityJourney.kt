package com.backbase.lookup.business_identity.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.validators.ButtonValidator
import com.backbase.android.flow.common.validators.ValidatorEmpty
import com.backbase.android.flow.common.validators.applyValidations
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.lookup.LookupJourney
import com.backbase.lookup.LookupRouter
import com.backbase.lookup.R
import com.backbase.lookup.business_identity.models.Industry
import com.backbase.lookup.business_identity.models.IndustryCollectionModel
import com.backbase.lookup.business_identity.ui.viewmodels.BusinessIdentityViewModel
import kotlinx.android.synthetic.main.journey_business_identity.*
import kotlinx.android.synthetic.main.screen_business_identity_business_description.*
import kotlinx.android.synthetic.main.screen_business_identity_company_website.*
import kotlinx.android.synthetic.main.screen_business_identity_industry.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

class BusinessIdentityJourney : Fragment(R.layout.journey_business_identity) {

    private var buttonValidator: ButtonValidator? = null
    private val viewModel: BusinessIdentityViewModel by inject()
    private var industriesMap = HashMap<String?, IndustryCollectionModel>()
    private val router: LookupRouter by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApis()
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

    private fun initViews() {
        viewModel.requestIndustries()
    }

    private fun submit() {
        industriesMap[txtIndustry.text.toString()]?.let {
            viewModel.submitBusinessIdentity(
                Industry(it.code, it.description),
                txtIndustry.text.toString(),
                txtCompanyWebsite.text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        initViews()
        stepPublisher.publish(LookupJourney.JourneyStepsLookup.BUSINESS_IDENTITY.value)
    }

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue,
            apiState = viewModel.apiSubmitBusinessIdentity.state
        )
        handleStateForRequestIdentities()
    }

    private fun handleStateForRequestIdentities() {
        handleStates(
            viewModel.apiRequestIdentities.state,
            { identities -> identities?.let { onFetchIdentities(it) } },
            { messageView.showErrorMessage("apiRequestIdentities", it.message) },
            null,
            {
                messageView.clearAllMessages()
            },
            btnContinue
        )
    }

    private fun onFetchIdentities(industries: List<IndustryCollectionModel>) {
        for (it in industries) {
            industriesMap[it.description] = it
        }
        txtIndustry.fill(
            requireContext(),
            industries.map { it.description }
        )
        txtIndustry.setText(requireContext().resources.getStringArray(R.array.industries)[0])
    }

    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton,
        apiState: ReceiveChannel<State<Any?>>
    ) {
        handleStates(
            apiState,
            { interactionResponse ->
                router.onBusinessIdentityFinished(interactionResponse)
            },
            {
                hintContainer.showHintFailure("${it.message}")
            },
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }


    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtIndustry.applyValidations(
                txtInputIndustry,
                ValidatorEmpty() to getString(R.string.lookup_journey_industry_is_missing)
            ),
            txtBusinessDescription.applyValidations(
                txtInputBusinessDescription,
                ValidatorEmpty() to getString(R.string.lookup_journey_business_description_is_missing)
            )
        )

}