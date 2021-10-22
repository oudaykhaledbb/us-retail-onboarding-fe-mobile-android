package com.backbase.lookup.business_info.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.validators.*
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.lookup.LookupJourney
import com.backbase.lookup.R
import com.backbase.lookup.ScreensLabel
import com.backbase.lookup.ScreensMonitor
import com.backbase.lookup.business_info.models.Item
import com.backbase.lookup.business_info.ui.InfoBottomSheet
import com.backbase.lookup.business_info.ui.viewmodels.BusinessInfoViewModel
import com.backbase.lookup.business_structure.module.BusinessDetailsResponseModel
import com.google.android.material.datepicker.CalendarConstraints
import kotlinx.android.synthetic.main.journey_business_info.*
import kotlinx.android.synthetic.main.screen_business_info_business_structure.*
import kotlinx.android.synthetic.main.screen_business_info_date_established.*
import kotlinx.android.synthetic.main.screen_business_info_dba.*
import kotlinx.android.synthetic.main.screen_business_info_ein.*
import kotlinx.android.synthetic.main.screen_business_info_legal_name.*
import kotlinx.android.synthetic.main.screen_business_info_state_operating_in.*
import okhttp3.internal.trimSubstring
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

private const val BUNDLE_TYPE = "BUNDLE_TYPE"
private const val BUNDLE_SUB_TYPE = "BUNDLE_SUB_TYPE"
private const val BUNDLE_BUSINESS_DETAILS = "BUNDLE_BUSINESS_DETAILS"

class BusinessInfoJourney : Fragment(R.layout.journey_business_info) {

    private var buttonValidator: ButtonValidator? = null
    private val viewModel: BusinessInfoViewModel by inject()
    private val screensMonitor: ScreensMonitor by inject()
    private lateinit var businessStructureMap: Map<String, List<String>>
    private var hasSubtype = true
    private var type: String? = null
    private var subType: String? = null
    private var businessDetails: BusinessDetailsResponseModel? = null
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = requireArguments().getString(BUNDLE_TYPE)
            subType = requireArguments().getString(BUNDLE_SUB_TYPE)
            businessDetails = requireArguments().getParcelable(BUNDLE_BUSINESS_DETAILS)
        }
    }

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

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(LookupJourney.JourneyStepsLookup.BUSINESS_INFO.value)
        viewModel.requestBusinessStructures()
        initViews()
    }

    private fun loadData() {
        type?.let { type ->
            txtBusinessStructure.setText(type)
            llStructureType.visibility = View.GONE
            subType?.let { subtype ->
                llStructureType.visibility = View.VISIBLE
                txtStructureType.setText(subtype)
            }
        }

        businessDetails?.details?.let { details ->
            txtLegalName.setText(details.legalName)
            txtEin.setText(details.ein.trimSubstring(0, 9))
            calendarDateEstablished.text = changeDateFormat(
                details.dateEstablished,
                "yyyy-MM-dd",
                calendarDateEstablished.dateFormat!!
            )
            details.stateOperatingIn?.let {
                txtStateOperatingIn.setText("$it")
            }
        }

    }


    @SuppressLint("NewApi")
    private fun changeDateFormat(
        originalDateString: String,
        initialDateFormat: String,
        toDateFormat: String
    ): String {
        val date = SimpleDateFormat(initialDateFormat).parse(originalDateString)
        return SimpleDateFormat(toDateFormat).format(date)
    }

    private fun initApis() {
        handleStateForSubmitApis(
            tappedButton = btnContinue
        )
    }

    private fun handleStateForSubmitApis(
        tappedButton: BackbaseButton
    ) {
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
            viewModel.apiSubmitBusinessDetails.state,
            {

                screensMonitor.navigate(
                    findNavController(),
                    R.id.action_to_business_address,
                    ScreensLabel.BusinessAddressScreen
                )
            },
            {
                hintContainer.showHintFailure("${it.message}")
            },
            { tappedButton.loading = true },
            { tappedButton.loading = false }
        )
    }

    private fun onFetchBusinessStructures(businessStructures: List<Item>) {
        this.businessStructureMap = businessStructures.associate { Pair(it.type, it.subtypes) }
        txtBusinessStructure.addTextChangedListener {
            businessStructureMap[it.toString()]?.let { lstTypes ->
                if (lstTypes.firstOrNull() == null) {
                    llStructureType.visibility = View.GONE
                    hasSubtype = false
                } else {
                    hasSubtype = true
                    llStructureType.visibility = View.VISIBLE
                    txtStructureType.setText("${lstTypes.firstOrNull()}")
                    txtStructureType.fill(requireContext(), lstTypes)
                }
            }
        }
        txtBusinessStructure.setText("${businessStructures.firstOrNull()?.type}")
        txtBusinessStructure.fill(requireContext(), businessStructures.map { it.type })
        loadData()

    }

    private fun submit() {
        viewModel.submitBusinessDetails(
            type = txtBusinessStructure.text.toString(),
            subType = if (hasSubtype) txtStructureType.text.toString() else null,
            legalName = txtLegalName.text.toString(),
            knownName = txtKnownName.text.toString(),
            ein = txtEin.text.toString().toIntOrNull(),
            establishedDate = manipulateEstablishedDate(
                calendarDateEstablished.text.toString(),
                calendarDateEstablished.dateFormat.toString()
            ),
            operationState = txtStateOperatingIn.text.toString(),
        )
    }

    fun manipulateEstablishedDate(originalDate: String, currentDateFormat: String): String {
        var formatter = SimpleDateFormat(currentDateFormat, Locale.ENGLISH)
        var date = formatter.parse(originalDate)
        val newSdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return "${newSdf.format(date)}T00:00:00.000Z"
    }

    private fun initValidators() =
        ButtonValidator(
            btnContinue,
            txtLegalName.applyValidations(
                txtInputLegalName,
                ValidatorEmpty() to getString(R.string.lookup_journey_Legal_name_is_missing)
            ),
            calendarDateEstablished.applyValidations(
                txtCalendarHelperText,
                ValidatorCalendarNotEmpty() to getString(R.string.lookup_journey_date_established_is_missing),
            ),
            txtEin.applyValidations(
                txtInputEin,
                ValidatorCharLength(9) to getString(R.string.lookup_journey_validation_ein_9_char)
            )
        )

    private fun initViews() {
        txtInputKnownName.getChildAt(1).visibility = View.GONE
        btnWhenDoYouNeedEin.setOnClickListener { openEinLink() }
        txtStateOperatingIn.setText(requireContext().resources.getStringArray(R.array.states)[0])
        txtStateOperatingIn.fill(
            requireContext(),
            requireContext().resources.getStringArray(R.array.states).toList()
        )
        calendarDateEstablished.setValidator(object : CalendarConstraints.DateValidator {
            override fun writeToParcel(dest: Parcel?, flags: Int) {

            }

            override fun isValid(date: Long) = System.currentTimeMillis() > date
            override fun describeContents() = 0
        })
        btnMoreInfo.setOnClickListener { showBottomSheet() }
    }

    private fun openEinLink() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.lookup_journey_ein_link)))
        startActivity(browserIntent)
    }

    private fun showBottomSheet() {
        InfoBottomSheet.newInstance().show(childFragmentManager, "InfoBottomSheet")
    }

}

fun getBusinessInfoBundle(
    type: String?,
    subType: String?,
    businessDetails: BusinessDetailsResponseModel?
) = Bundle().apply {
    putString(BUNDLE_TYPE, type)
    putString(BUNDLE_SUB_TYPE, subType)
    putParcelable(BUNDLE_BUSINESS_DETAILS, businessDetails)
}