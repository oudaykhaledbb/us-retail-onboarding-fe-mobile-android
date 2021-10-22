package com.backbase.android.flow.businessrelations.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.businessrelations.BusinessRelationsConfiguration
import com.backbase.android.flow.businessrelations.PersistentData
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.ui.viewmodel.RoleSelectionViewModel
import com.backbase.android.flow.common.extensions.gson
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.viewmodel.handleStates
import com.google.gson.Gson
import kotlinx.android.synthetic.main.screen_role_selection.*
import org.koin.android.ext.android.inject

class RoleSelectionScreen : Fragment(R.layout.screen_role_selection) {

    private var navController: NavController? = null
    private val viewModel: RoleSelectionViewModel by inject()
    private val businessRelationsConfiguration: BusinessRelationsConfiguration by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    private var tappedButton: BackbaseButton? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = parentFragment?.childFragmentManager?.fragments?.get(0)?.findNavController()
        initViews()
        handleViewModelStates()
        businessRelationsConfiguration.createCaseActionName?.let {
            viewModel.createCase(businessRelationsConfiguration.userInfoProvider.getUserInfo())
        }
    }

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsBusinessRelations.ROLE_SELECTION.value)
    }

    private fun handleViewModelStates() {
        handleStates(
            viewModel.apiSubmitRelationType.state,
            {
                navController?.navigate(R.id.action_to_createOwnerScreen,
                    PersistentData.currentUser?.let { it1 ->
                        CreateOwnerScreen.getBundle(
                            it1,
                            100
                        )
                    })
                return@handleStates Unit
            },
            { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() },
            { tappedButton?.loading = true },
            { tappedButton?.loading = false },
            btnAddOwners,
            btnAddOwnersIamController
        )

        handleStates(
            viewModel.apiCreateCase.state,
            {
                return@handleStates Unit
            },
            {
                btnAddOwners.text = it.message
            },
            { tappedButton?.loading = true },
            { tappedButton?.loading = false },
            btnAddOwners,
            btnAddOwnersIamController
        )

    }

    private fun initViews() {
        btnAddOwners.setOnClickListener{
            PersistentData.isCurrentUserTheControlPerson = false
            tappedButton = btnAddOwners //Handle loading button
            viewModel.submitRelationType(RelationType.OWNER) }
        btnAddOwnersIamController.setOnClickListener{
            PersistentData.isCurrentUserTheControlPerson = true
            tappedButton = btnAddOwnersIamController //Handle loading button
            viewModel.submitRelationType(RelationType.CONTROL_PERSON)
        }
    }

}