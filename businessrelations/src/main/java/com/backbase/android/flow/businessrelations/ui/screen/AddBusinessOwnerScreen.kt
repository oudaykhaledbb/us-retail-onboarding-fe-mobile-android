package com.backbase.android.flow.businessrelations.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.businessrelations.PersistentData
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.businessrelations.model.*
import com.backbase.android.flow.businessrelations.ui.viewmodel.AddBusinessOwnerViewModel
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.viewmodel.handleStates
import kotlinx.android.synthetic.main.screen_add_business_owner.*
import kotlinx.android.synthetic.main.view_owner_editable.view.*
import kotlinx.android.synthetic.main.view_owner_must_edit.view.*
import org.koin.android.ext.android.inject

class AddBusinessOwnerScreen : Fragment(R.layout.screen_add_business_owner) {

    private var owners = ArrayList<Owner>()
    private var navController: NavController? = null
    private val viewModel: AddBusinessOwnerViewModel by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = parentFragment?.childFragmentManager?.fragments?.get(0)?.findNavController()
        initViews()
        handleViewModelStates()
    }

    private fun initViews() {
        btnAddOwner.setOnClickListener {
            val dialogFragment =
                CreateOwnerScreen.getInstance(RelationType.OWNER, 100 - getTotalOwnership())
            dialogFragment.show(requireActivity().supportFragmentManager, "CreateOwnerScreen")
            dialogFragment.onDismissDialog = {
                viewModel.requestBusinessPersons(RelationType.OWNER)
            }
        }
        btnContinue.setOnClickListener { viewModel.completeOwnersStepActionName() }
        btnBack.setOnClickListener { navController?.popBackStack() }
    }

    private fun handleViewModelStates() {

        handleStates(
            viewModel.apiFetchOwners.state,
            { ownersResponse ->
                ownersResponse?.body?.let { owners ->
                    onFetchOwnerSuccessfully(
                        owners
                    )
                }
            },
            { messageView.showErrorMessage("apiFetchOwners", it.message) },
            null,
            {
                messageView.clearAllMessages()
                validateOwnership()
            },
            btnContinue,
            btnBack
        )

        handleStates(
            viewModel.apiFetchRoles.state,
            { rolesResponse -> rolesResponse?.body?.let { roles -> onFetchRolesSuccessfully(roles) } },
            { messageView.showErrorMessage("apiFetchRoles", it.message) },
            null,
            { messageView.clearAllMessages() },
            btnContinue,
            btnBack
        )

        handleStates(
            viewModel.apiCompleteOwnersStep.state,
            {
                onCompleteOwnerStep()
            },
            { messageView.showErrorMessage("apiCompleteOwnersStep", it.message) },
            null,
            { messageView.dismissMessage("apiCompleteOwnersStep") },
            btnContinue,
            btnBack
        )

    }

    private fun onCompleteOwnerStep() {
        val isCurrentUserTheControlPerson = PersistentData.isCurrentUserTheControlPerson
        if (isCurrentUserTheControlPerson) {
            navController?.navigate(R.id.action_to_summaryScreen)
        } else {
            navController?.navigate(R.id.action_to_selectControlPersonScreen)
        }
    }

    private fun onFetchRolesSuccessfully(roles: List<BusinessRoleModel>?) {
        PersistentData.roles = roles // Store roles to be used for later screens
        viewModel.requestBusinessPersons(RelationType.OWNER)
    }

    private fun onFetchOwnerSuccessfully(owners: List<Owner>?) {
        llOwnersContainer.removeAllViews()
        this@AddBusinessOwnerScreen.owners.clear()
        owners?.let { ownersList -> this@AddBusinessOwnerScreen.owners.addAll(ownersList) }
        owners?.forEach { owner ->
            if (owner.isCurrentUser == true && owner.role.isNullOrEmpty()) {
                llOwnersContainer.addView(
                    generateMustEditOwnerCard(owner)
                )
            } else {
                llOwnersContainer.addView(
                    generateEditableOwnerCard(owner)
                )
            }
        }
    }

    private fun validateOwnership() {
        if (getTotalOwnership() > 75) {
            messageView.showSuccessMessage("", getString(R.string.business_relations_all_owners_added))
            btnAddOwner.visibility = View.GONE
        } else {
            btnAddOwner.visibility = View.VISIBLE
        }
    }


    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsBusinessRelations.Add_BUSINESS_OWNER.value)
        viewModel.requestBusinessRolesAction()
    }

    private fun getTotalOwnership(): Int {
        var total = 0
        owners.forEach { total += it.ownershipPercentage?:0 }
        return total
    }

    private fun generateEditableOwnerCard(owner: Owner): View =
        layoutInflater.inflate(R.layout.view_owner_editable, null, false).apply {
            lblOwnerName.text = owner.fullName()
            lblOwnerInfo.text = String.format(
                getString(R.string.business_relations_owner_position_with_percentage),
                owner.findRole(),
                owner.ownershipPercentage
            )
            btnEdit.setOnClickListener {
                val dialogFragment =
                    CreateOwnerScreen.getInstance(
                        owner,
                        100 - getTotalOwnership() + (owner.ownershipPercentage?:0)
                    )
                dialogFragment.onDismissDialog = {
                    viewModel.requestBusinessPersons(RelationType.OWNER)
                }
                dialogFragment.show(requireActivity().supportFragmentManager, "CreateOwnerScreen")
            }
        }

    private fun generateMustEditOwnerCard(owner: Owner): View =
        layoutInflater.inflate(R.layout.view_owner_must_edit, null, false).apply {
            lblMustEditOwnerName.text = owner.fullName()
            btnMustEdit.setOnClickListener {
                val dialogFragment =
                    CreateOwnerScreen.getInstance(
                        owner,
                        100 - getTotalOwnership() + (owner.ownershipPercentage?:0)
                    )
                dialogFragment.onDismissDialog = {
                    viewModel.requestBusinessPersons(RelationType.OWNER)
                }
                dialogFragment.show(requireActivity().supportFragmentManager, "CreateOwnerScreen")
            }
        }

}
