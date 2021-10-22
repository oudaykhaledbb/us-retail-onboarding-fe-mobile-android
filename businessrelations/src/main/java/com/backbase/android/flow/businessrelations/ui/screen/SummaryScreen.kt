package com.backbase.android.flow.businessrelations.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.businessrelations.BusinessRelationsRouter
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.businessrelations.model.*
import com.backbase.android.flow.businessrelations.ui.viewmodel.AddBusinessOwnerViewModel
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.viewmodel.handleStates
import kotlinx.android.synthetic.main.screen_add_business_owner.*
import kotlinx.android.synthetic.main.screen_fragment_select_control_person.btnContinue
import kotlinx.android.synthetic.main.view_info_regular_owner.view.*
import kotlinx.android.synthetic.main.view_owner_editable.view.lblOwnerInfo
import kotlinx.android.synthetic.main.view_owner_editable.view.lblOwnerName
import org.koin.android.ext.android.inject

class SummaryScreen : Fragment(R.layout.summary_screen) {

    private var navController: NavController? = null
    private val viewModel: AddBusinessOwnerViewModel by inject()
    private val router: BusinessRelationsRouter by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = parentFragment?.childFragmentManager?.fragments?.get(0)?.findNavController()
        initViews()
        handleViewModelStates()
    }

    private fun initViews() {
        btnContinue.setOnClickListener { viewModel.completeSummaryStep() }
        btnBack.setOnClickListener { navController?.popBackStack() }
    }

    private fun handleViewModelStates() {
        handleStates(viewModel.apiFetchOwners.state, { ownersResponse ->
            ownersResponse?.body?.let { owners ->
                llOwnersContainer.removeAllViews()
                owners?.forEach { owner ->
                    if (owner.relationType() == RelationType.OWNER) llOwnersContainer.addView(
                        generateMustEditOwnerCard(owner)
                    )
                    else llOwnersContainer.addView(generateEditableOwnerCard(owner))
                }
            }
            return@handleStates Unit
        }, {})

        handleStates(
            viewModel.apiCompleteSummaryStep.state,
            { router.onBusinessRelationsFinished() },
            { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() },
            null,
            null,
            btnContinue,
            btnBack
        )

    }

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsBusinessRelations.SELECT_CONTROL_PERSON.value)
        viewModel.requestBusinessPersons()
    }

    private fun generateEditableOwnerCard(owner: Owner): View =
        layoutInflater.inflate(R.layout.view_info_regular_owner, null, false).apply {
            lblOwnerName.text = owner.fullName()
            lblOwnerInfo.text =
                if (!owner.controlPerson)
                    String.format(
                        getString(R.string.business_relations_owner_position_with_percentage),
                        owner.findRole(),
                        owner.ownershipPercentage
                    ) else getString(R.string.business_relations_control_person)
            btnInfo.setOnClickListener { showBottomSheet(owner) }
        }

    private fun generateMustEditOwnerCard(owner: Owner): View =
        layoutInflater.inflate(R.layout.view_info_control_person, null, false).apply {
            lblOwnerName.text = owner.fullName()
            lblOwnerInfo.text = String.format(
                getString(R.string.business_relations_owner_position_with_percentage),
                owner.findRole(),
                owner.ownershipPercentage
            )
            btnInfo.setOnClickListener { showBottomSheet(owner) }
        }

    private fun showBottomSheet(owner: Owner) {
        InfoBottomSheet.newInstance(owner).show(childFragmentManager, "InfoBottomSheet")
    }

}
