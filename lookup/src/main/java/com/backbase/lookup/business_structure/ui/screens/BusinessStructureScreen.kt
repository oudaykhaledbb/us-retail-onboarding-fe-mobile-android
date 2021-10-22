package com.backbase.lookup.business_structure.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.*
import com.backbase.lookup.business_structure.module.BusinessStructureResponseModel
import com.backbase.lookup.business_structure.module.Item
import com.backbase.lookup.business_structure.ui.viewmodels.BusinessStructureViewModel
import kotlinx.android.synthetic.main.screen_business_structure.*
import org.koin.android.ext.android.inject

internal const val BUNDLE_TYPE = "BUNDLE_TYPE"
internal const val BUNDLE_SUB_TYPE = "BUNDLE_SUB_TYPE"

class BusinessStructureScreen : Fragment(R.layout.screen_business_structure) {

    private lateinit var type: String
    private var subtype: String? = null
    private lateinit var businessStructureMap: Map<String, List<String>>
    private val viewModel: BusinessStructureViewModel by inject()
    private val router: LookupRouter by inject()
    private val screensMonitor: ScreensMonitor by inject()
    private val stepPublisher: StepInfoPublisher by inject()
    private var hasSubtype = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleState()
        setupSubmitButton()
    }

    private fun setupSubmitButton() {
        btnContinue.setOnClickListener {
            type = txtBusinessStructure.text.toString()
            subtype = if (hasSubtype) txtStructureType.text.toString() else null
            viewModel.submitSelectedBusinessStructure(type, subtype)
        }
    }

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(LookupJourney.JourneyStepsLookup.BUSINESS_STRUCTURE.value)
        viewModel.requestBusinessStructures()
    }

    private fun handleState() {
        handleStates(
            viewModel.apiRequestBusinessStructure.state,
            { businessStructures -> businessStructures?.let { onFetchBusinessStructures(it) } },
            { messageView.showErrorMessage("apiRequestBusinessStructure", it.message) },
            null,
            {
                messageView.clearAllMessages()
            },
            btnContinue
        )
        handleStates(
            viewModel.apiSubmitSelectedBusinessStructure.state,
            { businessStructureResponseModel ->
                onBusinessStructureSubmitedSuccessfully(businessStructureResponseModel)
            },
            { messageView.showErrorMessage("apiSubmitSelectedBusinessStructure", it.message) },
            null,
            {
                messageView.clearAllMessages()
            },
            btnContinue
        )
    }

    private fun onBusinessStructureSubmitedSuccessfully(businessStructureResponseModel: InteractionResponse<BusinessStructureResponseModel?>?) {
        businessStructureResponseModel?.body?.performCompanyLookup?.let {

            if (it)screensMonitor.navigate(findNavController(), R.id.action_to_companyLookupScreen, ScreensLabel.SCREEN_COMPANY_LOOKUP, Bundle().apply {
                putString(BUNDLE_TYPE, type)
                putString(BUNDLE_SUB_TYPE, subtype)
            }) else{
                screensMonitor.navigate(findNavController(), R.id.action_to_business_info, ScreensLabel.BusinessInfoJourney)
            }

        }
    }

    private fun onFetchBusinessStructures(businessStructures: List<Item>) {
        this.businessStructureMap = businessStructures.associate { Pair(it.type, it.subtypes) }
        txtBusinessStructure.addTextChangedListener {
            businessStructureMap[it.toString()]?.let { lstTypes ->
                if (lstTypes.firstOrNull() == null){
                    llStructureType.visibility = View.GONE
                    hasSubtype = false
                }else{
                    hasSubtype = true
                    llStructureType.visibility = View.VISIBLE
                    txtStructureType.setText("${lstTypes.firstOrNull()}")
                    txtStructureType.fill(requireContext(), lstTypes)
                }
            }
        }
        txtBusinessStructure.setText("${businessStructures.firstOrNull()?.type}")
        txtBusinessStructure.fill(requireContext(), businessStructures.map { it.type })
    }

}