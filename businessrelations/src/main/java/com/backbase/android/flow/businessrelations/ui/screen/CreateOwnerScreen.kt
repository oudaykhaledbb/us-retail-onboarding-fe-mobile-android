package com.backbase.android.flow.businessrelations.ui.screen

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.businessrelations.BusinessRelationsConfiguration
import com.backbase.android.flow.businessrelations.PersistentData
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.businessrelations.model.Owner
import com.backbase.android.flow.businessrelations.model.RelationType
import com.backbase.android.flow.businessrelations.model.fullName
import com.backbase.android.flow.businessrelations.model.relationType
import com.backbase.android.flow.businessrelations.ui.viewmodel.AddBusinessOwnerViewModel
import com.backbase.android.flow.businessrelations.usecase.DEFAULT_ROLE
import com.backbase.android.flow.common.state.State
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.screen_create_owner.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.koin.android.ext.android.inject

private const val TAG_OWNER = "TAG_OWNER"
private const val TAG_MAX_ALLOWED_PERCENTAGE = "TAG_MAX_ALLOWED_PERCENTAGE"
private const val SCREEN_STYPE = "SCREEN_STYPE"
private const val MIN_OWNERSHIP = 25

class CreateOwnerScreen : DialogFragment() {

    private lateinit var currencyAdapter: ArrayAdapter<String>
    private var owner: Owner? = null
    private var maxOwnershipAllowed: Int = 100
    var onDismissDialog: ((dialog: Dialog) -> Unit)? = null
    private val viewModel: AddBusinessOwnerViewModel by inject()
    private val configuration: BusinessRelationsConfiguration by inject()
    private var buttonValidator: ButtonValidator? = null
    private var screenStyle = ScreenStyle.Standalone

    private val interactiveViews by lazy {
        arrayListOf(
            txtOwnershipPercentage,
            txtRole,
            txtMobileNumber,
            textEmail,
            textLastName,
            textFirstName,
            currencyInputEditText,
            imgClose,
            btnRemoveControlPerson,
            btnRemoveOwner,
            btnContinue,
            btnBack
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        getDialog()?.let { onDismissDialog?.invoke(it) }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        getDialog()?.let { onDismissDialog?.invoke(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_create_owner, container, false)
    }


    private fun disableFieldsIfStandaloneScreen(){
        if (screenStyle == ScreenStyle.Dialog) return
        if (configuration.enableCurrentUserEditing) return
        txtMobileNumber.isEnabled = false
        textEmail.isEnabled = false
        textLastName.isEnabled = false
        textFirstName.isEnabled = false
    }

    private fun initValidators() =
        if (owner?.relationType() != RelationType.OWNER) {
            ButtonValidator(
                btnContinue,
                firstNameValidator(),
                lastNameValidator(),
                emailValidator(),
                mobileNumberValidator()
            )
        } else {
            ButtonValidator(
                btnContinue,
                firstNameValidator(),
                lastNameValidator(),
                emailValidator(),
                mobileNumberValidator(),
                ownershipPercentageValidator(),
                roleValidator()
            )
        }


    private fun ownershipPercentageValidator(): ValidatorResult {
        return txtOwnershipPercentage.applyValidations(
            txtInputOwnershipPercentage,
            ValidatorEmpty() to getString(R.string.business_relations_label_ownership_is_required),
            ValidatorMinInt(MIN_OWNERSHIP) to getString(R.string.business_relations_label_requirement_25_percent),
            ValidatorMaxInt(maxOwnershipAllowed) to getString(R.string.business_relations_label_ownership_exceed_100)
        )
    }

    private fun mobileNumberValidator(): ValidatorResult {
        return txtMobileNumber.applyValidations(
            textInputMobileNumber,
            ValidatorEmpty() to getString(R.string.business_relations_label_mobile_number_required),
            ValidatorMinChar(10) to getString(R.string.business_relations_label_mobile_number_must_be_10_digits),
            ValidatorMaxChar(10) to getString(R.string.business_relations_label_mobile_number_must_be_10_digits)
        )
    }

    private fun emailValidator(): ValidatorResult {
        return textEmail.applyValidations(
            textInputEmail,
            ValidatorEmpty() to getString(R.string.business_relations_label_email_required),
            ValidatorEmail() to getString(R.string.business_relations_label_email_not_valid)
        )
    }

    private fun lastNameValidator(): ValidatorResult {
        return textLastName.applyValidations(
            textInputLastName,
            ValidatorEmpty() to getString(R.string.business_relations_label_last_name_required)
        )
    }

    private fun firstNameValidator(): ValidatorResult {
        return textFirstName.applyValidations(
            textInputFirstName,
            ValidatorEmpty() to getString(R.string.business_relations_first_name_required)
        )
    }

    private fun roleValidator(): ValidatorResult {
        return currencyInputEditText.applyValidations(
            txtRole,
            ValidatorEmpty() to getString(R.string.business_relations_label_field_required)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (screenStyle == ScreenStyle.Standalone){
            llHeader.visibility = View.GONE
        }
        initApis()
        owner?.apply { initViewsForEditMode(this) }
        if (owner?.relationType() != RelationType.OWNER) {
            txtOwnershipPercentage.visibility = View.GONE
            lblAvailablePercentage.visibility = View.GONE
            otherRoleInput.visibility = View.GONE
        }else{
            initLabelAvailableOwnership()
            initRoleEditText()

        }
        initButtons()
        owner?.let { fillOwnerData(it) }
        setupRolesSpinner()
        disableFieldsIfStandaloneScreen()
    }

    private fun initContinueButton() {
        btnContinue.setOnClickListener {
            if (buttonValidator == null) {
                this.buttonValidator = initValidators()
            }
            if (btnContinue.isEnabled) {
                submit()
            }
        }
    }

    private fun setupRolesSpinner() {
        PersistentData.roles?.let { businessRoles ->
            currencyAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    businessRoles.map { it.name }
                )
            currencyInputEditText.setAdapter(currencyAdapter)
        }
    }

    private fun initRoleEditText() {
        txtRole.editText?.addTextChangedListener { text ->
            otherRoleInput.visibility =
                if (text.toString().toLowerCase()
                        .contentEquals(DEFAULT_ROLE)
                ) View.VISIBLE else View.GONE
        }
    }

    private fun initLabelAvailableOwnership() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(txtOwnershipPercentage.textChanges().subscribe {
            lblAvailablePercentage.visibility = View.GONE
            lblAvailablePercentage.text = ""
            if (it.isNullOrEmpty()) return@subscribe
            lblAvailablePercentage.visibility = View.VISIBLE
            lblAvailablePercentage.text = String.format(
                getString(R.string.business_relations_ownership_available),
                (maxOwnershipAllowed - it.toString().toInt())
            )
            it.toString().toInt()
        })
    }

    private fun initViewsForEditMode(owner: Owner) {
        owner.apply {
            btnRemoveOwner.visibility = View.GONE
            btnContinue.text = getString(R.string.business_relations_save)
            if (relationType() != RelationType.OWNER) {
                btnRemoveControlPerson.visibility = View.VISIBLE
                llOwnershipPercentage.visibility = View.GONE
                lblOwnershipPercentage.visibility = View.GONE
                lblAvailablePercentage.visibility = View.GONE
            }

            if (isCurrentUser == false && !role.isNullOrEmpty()) btnRemoveOwner.visibility =
                View.VISIBLE

            if (relationType() != RelationType.OWNER) {
                lblTitle.text = getString(R.string.business_relations_add_a_control_person)
                lblSubtitle.text =
                    getString(R.string.business_relations_all_information_needed_to_create_control_person)
            }

            if (id?.isNotEmpty() == true) {
                lblTitle.text = fullName()
            }
        }
    }

    private fun submit() = getOwner()?.apply{
        if (screenStyle == ScreenStyle.Standalone){
            viewModel.updateOwner(this, PersistentData.currentUser?.relationType())
        }else{
            if (owner?.relationTypeStr == RelationType.OWNER.toString()) viewModel.updateOwner(this, null)
            else viewModel.updateControlPerson(apply {
                controlPerson = true
                relationTypeStr = RelationType.CONTROL_PERSON.toString()
            })
        }
    }

    private fun initButtons() {
        btnRemoveControlPerson.visibility = View.GONE
        btnBack.setOnClickListener { exitScreen() }
        imgClose.setOnClickListener { exitScreen() }
        btnRemoveOwner.setOnClickListener { owner?.id?.let { id -> removeOwner(id) } }
        btnRemoveControlPerson.setOnClickListener {
            owner?.id?.let { id ->
                removeControlPerson((id))
            }
        }
        initContinueButton()
    }

    private fun removeOwner(id: String) {
        showWarningMessage(R.string.business_relations_remove_this_owner, R.string.business_relations_owner_will_be_deleted_permanently) {
            viewModel.deleteOwner(id)
        }
    }

    private fun removeControlPerson(id: String) {
        showWarningMessage(
            R.string.business_relations_remove_control_person,
            R.string.business_relations_owner_will_be_deleted_permanently
        ) {
            viewModel.deleteOwner(id)
        }
    }

    private fun showWarningMessage(title: Int, message: Int, onRemoveListener: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.business_relations_remove) { _, _ -> onRemoveListener.invoke() }
            .setNegativeButton(R.string.business_relations_keep) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun initApis() {
        handleStateForSubmitApis(tappedButton = btnRemoveOwner, apiState = viewModel.apiDeleteOwner.state)
        handleStateForSubmitApis(tappedButton = btnRemoveControlPerson, apiState = viewModel.apiDeleteControlPerson.state)
        handleStateForSubmitApis(tappedButton = btnContinue, apiState = viewModel.apiUpdateOwner.state)
        handleStateForSubmitApis(tappedButton = btnContinue, apiState = viewModel.apiUpdateControlPerson.state)
    }

    private fun handleStateForSubmitApis(tappedButton: BackbaseButton, apiState: ReceiveChannel<State<Any?>>) {
        handleStates(
            apiState,
            {
                exitScreen()
            },
            { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show() },
            { tappedButton.loading = true },
            { tappedButton.loading = false },
            interactiveViews
        )
    }

    private fun exitScreen(){
        if (screenStyle == ScreenStyle.Dialog){
            dismissAllowingStateLoss()
        }
        else{
            val navController = parentFragment?.childFragmentManager?.fragments?.get(0)?.findNavController()
            navController?.navigate(R.id.action_to_addBusinessOwnerScreen)
        }
    }

    private fun fillOwnerData(owner: Owner) {
        textFirstName.text = owner.firstName.toEditable()
        textLastName.text = owner.lastName.toEditable()
        textEmail.text = owner.email.toEditable()
        owner.phone?.let {
            txtMobileNumber.text = owner.phone.toString()
                .replace("+1", "")
                .replace("-", "")
                .toEditable()
        }
        owner.role?.let {
            txtRole.editText?.text = owner.role.toEditable()
        }
        owner.ownershipPercentage?.let {
            txtOwnershipPercentage.text = "${owner.ownershipPercentage}".toEditable()
        }
    }

    private fun getOwner() = owner?.apply {
        firstName = textFirstName.text.toString()
        lastName = textLastName.text.toString()
        email = textEmail.text.toString()
        phone = "+1${txtMobileNumber.text.toString()}"
        role = txtRole.editText?.text.toString()
        otherRole = if (role?.toLowerCase().toString()
                .contentEquals(DEFAULT_ROLE)
        ) "" else otherRoleInput.editText?.text.toString()
        ownershipPercentage = txtOwnershipPercentage.text.toString().toInt()
    }


    companion object {

        fun getInstance(owner: Owner, maxOwnershipAllowed: Int) = CreateOwnerScreen().apply {
            arguments = getBundleInternal(owner, maxOwnershipAllowed, ScreenStyle.Dialog)
        }

        fun getInstance(relationType: RelationType, maxOwnershipAllowed: Int) =
            CreateOwnerScreen().apply {
                arguments = getBundleInternal(relationType, maxOwnershipAllowed, ScreenStyle.Dialog)
            }

        fun getBundle(owner: Owner, maxOwnershipAllowed: Int) =
            getBundleInternal(owner, maxOwnershipAllowed, ScreenStyle.Standalone)

        fun getBundle(
            relationType: RelationType,
            maxOwnershipAllowed: Int
        ) = getBundleInternal(relationType, maxOwnershipAllowed, ScreenStyle.Standalone)

        private fun getBundleInternal(
            owner: Owner,
            maxOwnershipAllowed: Int,
            screenStyle: ScreenStyle
        ) =
            Bundle().apply {
                putParcelable(TAG_OWNER, owner)
                putInt(TAG_MAX_ALLOWED_PERCENTAGE, maxOwnershipAllowed)
                putSerializable(SCREEN_STYPE, screenStyle)
            }

        private fun getBundleInternal(
            relationType: RelationType,
            maxOwnershipAllowed: Int,
            screenStyle: ScreenStyle
        ) = Bundle().apply {
            putParcelable(
                TAG_OWNER,
                Owner().apply { this.relationTypeStr = relationType.toString() })
            putInt(TAG_MAX_ALLOWED_PERCENTAGE, maxOwnershipAllowed)
            putSerializable(SCREEN_STYPE, screenStyle)
        }
    }

    override fun getTheme(): Int {
        return if (screenStyle == ScreenStyle.Dialog) {
            R.style.Theme_Backbase_Fullscreen
        } else super.getTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        owner = arguments?.getParcelable(TAG_OWNER)
        arguments?.getInt(TAG_MAX_ALLOWED_PERCENTAGE)?.let {
            maxOwnershipAllowed = it
        }
        (arguments?.getSerializable(SCREEN_STYPE) as ScreenStyle?)?.let {
            screenStyle = it
        }
        if (screenStyle == ScreenStyle.Dialog) {
            setStyle(
                STYLE_NO_FRAME,
                R.style.FullScreenDialog
            )
        }
    }

    enum class ScreenStyle { Standalone, Dialog }
}

private fun String?.toEditable() = Editable.Factory.getInstance().newEditable(this)


class ValidatorMaxInt(private val max: Int) : Validator {
    override fun validate(input: String?): Boolean {
        return if (input.isNullOrEmpty()) true else input.toString().toInt() <= max
    }
}

class ValidatorMinInt(private val min: Int) : Validator {
    override fun validate(input: String?): Boolean {
        return if (input.isNullOrEmpty()) true else input.toString().toInt() >= min
    }
}