package com.backbase.android.flow.businessrelations.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.model.findRole
import com.backbase.android.flow.businessrelations.model.fullName
import com.backbase.android.flow.businessrelations.ui.viewmodel.SelectControlPersonViewModel
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.viewmodel.handleStates
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.android.synthetic.main.screen_add_business_owner.llOwnersContainer
import kotlinx.android.synthetic.main.screen_fragment_select_control_person.*
import kotlinx.android.synthetic.main.screen_fragment_select_control_person.btnBack
import kotlinx.android.synthetic.main.screen_fragment_select_control_person.btnContinue
import kotlinx.android.synthetic.main.view_controller.view.*
import org.koin.android.ext.android.inject

class SelectControlPersonScreen : Fragment(R.layout.screen_fragment_select_control_person) {

    private var navController: NavController? = null
    private val radioGroup = RadioGroup()
    private val viewModel: SelectControlPersonViewModel by inject()
    private var selectedOwner: Owner? = null
    private val stepPublisher: StepInfoPublisher by inject()

    private val interactiveViews by lazy {
        arrayListOf<View>(
            btnControlPerson,
            btnBack
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = parentFragment?.childFragmentManager?.fragments?.get(0)?.findNavController()
        handleViewModelStates()

        btnControlPerson.setOnClickListener {
            val dialogFragment = CreateOwnerScreen.getInstance(RelationType.CONTROL_PERSON, 0)
            dialogFragment.onDismissDialog = {
                viewModel.requestBusinessPersons()
            }
            dialogFragment.show(requireActivity().supportFragmentManager, "CreateOwnerScreen")
        }

        btnContinue.setOnClickListener {
            selectedOwner?.id?.let { owner ->
                viewModel.submitSelectedControlPerson(
                    owner
                )
            }
        }
        btnBack.setOnClickListener { navController?.popBackStack() }
    }

    private fun handleViewModelStates() {
        handleStates(
            viewModel.apiFetchOwners.state, { ownersResponse ->
                ownersResponse?.body?.let { owners ->
                    llOwnersContainer.removeAllViews()
                    handleBtnAddControlPerson(owners)
                    owners?.forEach { owner ->
                        llOwnersContainer.addView(generateOwnerCard(owner))
                    }
                    btnContinue.isEnabled = false
                    llOwnersContainer.children.iterator().forEach {
                        val owner = owners?.get(llOwnersContainer.indexOfChild(it))
                        if (owner?.controlPerson) it.rbControlPerson.isChecked = true
                    }
                }
                return@handleStates Unit
            },
            { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() },
            {},
            {},
            getActionView()
        )

        handleStates(
            viewModel.apiSelectControlPerson.state,
            {
                navigateToSummaryScreen()
                return@handleStates Unit
            },
            { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() },
            { btnContinue.loading = true },
            { btnContinue.loading = false },
            getActionView()
        )
    }

    private fun handleBtnAddControlPerson(owners: List<Owner>?) {
        val hasControlNonOwnerPerson = owners?.find { it.ownershipPercentage == 0 } != null
        btnControlPerson.visibility = if (hasControlNonOwnerPerson) View.GONE else View.VISIBLE
    }

    private fun getActionView() = arrayListOf<View>().apply {
        addAll(interactiveViews)
        addAll(radioGroup.getViews())
    }

    private fun navigateToSummaryScreen() {
        navController?.navigate(R.id.action_to_summaryScreen)
    }

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsBusinessRelations.SELECT_CONTROL_PERSON.value)
        viewModel.requestBusinessPersons()
    }

    private fun generateOwnerCard(owner: Owner): View =
        layoutInflater.inflate(R.layout.view_controller, null, false).apply {
            lblEditControllerName.text = owner.fullName()
            if (owner.ownershipPercentage == 0) {
                btnMustEdit.visibility = View.VISIBLE
                lblRoleEditable.text = getString(R.string.business_relations_control_person)
                btnMustEdit.setOnClickListener {
                    val dialogFragment =
                        CreateOwnerScreen.getInstance(
                            owner,
                            0
                        )
                    dialogFragment.onDismissDialog = {
                        viewModel.requestBusinessPersons()
                    }
                    dialogFragment.show(
                        requireActivity().supportFragmentManager,
                        "CreateOwnerScreen"
                    )
                }
            } else {
                btnMustEdit.visibility = View.GONE
                lblRoleEditable.text = String.format(
                    getString(R.string.business_relations_owner_position_with_percentage),
                    owner.findRole(),
                    owner.ownershipPercentage
                )
            }
            radioGroup.addRadioButton(rbControlPerson)
            rbControlPerson.attachToView(root) { isChecked ->
                if (isChecked) {
                    radioGroup.notifyAll(rbControlPerson)
                    selectedOwner = owner
                    btnContinue.isEnabled = true
                }
            }
        }

}

private fun MaterialRadioButton.attachToView(view: View, onCheckListener: ((Boolean) -> Unit)) {
    this.setOnCheckedChangeListener { _, isChecked ->
        view.setBackgroundResource(if (isChecked) R.drawable.background_owner_card_selected else R.drawable.background_owner_card_none)
        onCheckListener.invoke(isChecked)
    }
    view.setOnClickListener { this.isChecked = !this.isChecked }
}

private class RadioGroup {
    private var radioButtons = ArrayList<MaterialRadioButton>()

    fun addRadioButton(radioButton: MaterialRadioButton) {
        radioButtons.add(radioButton)
    }

    fun notifyAll(selectedRadioButton: MaterialRadioButton) {
        radioButtons.forEach { if (it != selectedRadioButton) it.isChecked = false }
    }

    fun getViews() = radioButtons

}