package com.backbase.lookup.business_structure.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.android.flow.common.extensions.fill
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.common.validators.ButtonValidator
import com.backbase.android.flow.common.validators.ValidatorEmpty
import com.backbase.android.flow.common.validators.ValidatorMinChar
import com.backbase.android.flow.common.validators.applyValidations
import com.backbase.android.flow.common.viewmodel.handleStates
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.lookup.LookupJourney
import com.backbase.lookup.R
import com.backbase.lookup.ScreensLabel
import com.backbase.lookup.ScreensMonitor
import com.backbase.lookup.business_info.ui.screens.getBusinessInfoBundle
import com.backbase.lookup.business_structure.module.*
import com.backbase.lookup.business_structure.ui.viewmodels.CompanyLookupViewModel
import kotlinx.android.synthetic.main.screen_company_lookup.*
import org.koin.android.ext.android.inject

class CompanyLookupScreen() : Fragment(R.layout.screen_company_lookup), OnCompanySelectedListener {

    private var countryStateMap: HashMap<String, List<State>> = hashMapOf()
    private var countriesMap: HashMap<String, Country> = hashMapOf()
    private var stateMap: HashMap<String, State> = hashMapOf()
    private var selectedCountry: Country? = null
    private var selectedState: State? = null
    private var selectedBusiness: BusinessModel? = null
    private var buttonValidator: ButtonValidator? = null
    private val viewModel: CompanyLookupViewModel by inject()
    private val screensMonitor: ScreensMonitor by inject()
    private val stepPublisher: StepInfoPublisher by inject()

    private var type: String? = null
    private var subType: String? = null

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(LookupJourney.JourneyStepsLookup.COMPANY_LOOKUP.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = requireArguments().getString(BUNDLE_TYPE)
        subType = requireArguments().getString(BUNDLE_SUB_TYPE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleState()
        viewModel.requestCountries()
        initActionButtons()
        notifyBusinessSelectionChanged(selectedBusiness)
    }

    private fun initValidators() =
        ButtonValidator(
            btnFindBusiness,
            txtBusinessName.applyValidations(
                txtInputBusinessName,
                ValidatorEmpty() to getString(R.string.lookup_journey_validation_business_name_3_char),
                ValidatorMinChar(3) to getString(R.string.lookup_journey_validation_business_name_3_char)
            )
        )


    private fun initActionButtons() {
        btnFindBusiness.setOnClickListener {
            if (buttonValidator == null) {
                this.buttonValidator = initValidators()
            }
            if (btnFindBusiness.isEnabled) {
                requestCompanyLookup()
            }
        }
        btnClear.setOnClickListener {
            selectedBusiness = null
            notifyBusinessSelectionChanged(selectedBusiness)
        }

        btnContinue.setOnClickListener {
            selectedBusiness?.let {
                viewModel.submitCompanyDetails(
                    CompanyDetailsModel(it.jurisdictionCode, it.number)
                )
            }
        }
    }

    private fun requestCompanyLookup(){
        selectedCountry?.let { country ->
            viewModel.requestCompanyLookup(
                LookupModel(
                    country.isoCode,
                    selectedState?.isoCode,
                    txtBusinessName.text.toString()
                )
            )
        }
    }

    private fun handleState() {
        handleStates(
            viewModel.apiRequestCountries.state,
            { countriesModel -> countriesModel?.let { onFetchCounries(it) } },
            { messageView.showErrorMessage("apiRequestCountries", it.message) },
            null,
            {
                messageView.clearAllMessages()
            },
            btnFindBusiness
        )
        handleStates(
            viewModel.apiRequestCompanyLookup.state,
            { businesses -> onFetchBusinesses(businesses) },
            { onFetchBusinesses(null)},
            null,
            {
                messageView.clearAllMessages()
            },
            btnFindBusiness
        )

        handleStates(
            viewModel.apiSubmitCompanyDetails.state,
            { interactionResponse -> onSubmitCompanyDetails(interactionResponse) },
            { messageView.showErrorMessage("apiSubmitCompanyDetails", it.message) },
            null,
            {
                messageView.clearAllMessages()
            },
            btnFindBusiness
        )
    }

    private fun onFetchBusinesses(businesses: InteractionResponse<List<BusinessModel>?>?) {
        if (businesses == null){
            val noCompanyFoundScreen =
                NoCompanyFoundScreen.getInstance { skipAndEnterManually() }
            noCompanyFoundScreen.show(requireActivity().supportFragmentManager, "NoCompanyFoundScreen")
        }else{
            val selectBusinessScreen =
                SelectBusinessScreen.getInstance(this, businesses.body)
            selectBusinessScreen.show(requireActivity().supportFragmentManager, "SelectBusinessScreen")
        }
    }

    private fun onFetchCounries(countries: List<Country>) {
        countryStateMap.clear()
        for (it in countries) {
            countryStateMap[it.name] = it.states
            countriesMap[it.name] = it
        }
        initViews()
    }


    private fun initViews(){
        txtBusinessLocation.addTextChangedListener { country ->
            val statesList = countryStateMap[country.toString()]?.toList()?.map { it.name }
            txtState.setText(statesList?.firstOrNull() ?: "")
            statesList?.let { states ->
                txtState.fill(
                    requireContext(),
                    states
                )
            }
            llState.visibility =
                if (statesList?.isNullOrEmpty() != true) View.VISIBLE else View.GONE
            selectedCountry = countriesMap[country.toString()]
            stateMap.clear()
            selectedCountry?.states?.apply {
                for (it in this) {
                    stateMap[it.name] = it
                }
            }
        }

        txtState.addTextChangedListener { stateName ->
            selectedState = stateMap[stateName.toString()]
        }

        txtBusinessLocation.setText(countryStateMap.keys.firstOrNull())

        txtState.setText(countryStateMap.values.firstOrNull()?.firstOrNull()?.name)

        txtBusinessLocation.fill(
            requireContext(),
            countryStateMap.keys.toList()
        )

    }

    private fun skipAndEnterManually(){
        Log.d("CompanyLookupScreen", "Skip and Enter manually")
    }

    private fun onSubmitCompanyDetails(interactionResponse: InteractionResponse<BusinessDetailsResponseModel?>?) {
        screensMonitor.navigate(findNavController(), R.id.action_to_business_info, ScreensLabel.BusinessInfoJourney, getBusinessInfoBundle(type, subType, interactionResponse?.body))
    }

    override fun onCompanySelected(business: BusinessModel?) {
        this.selectedBusiness = business
        notifyBusinessSelectionChanged(business)
    }

    private fun setupVisibilityBasedOnContent(vararg textViews: TextView){
        textViews.forEach {
            it.visibility = if (it.text.toString().isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }
    private fun notifyBusinessSelectionChanged(company: BusinessModel?) {
        setupButtonVisibility(isCompanySelected = company!=null)
        showNotificationForSelectedCompany(company)
    }

    private fun showNotificationForSelectedCompany(company: BusinessModel?){
        llSelectedCompany.isVisible = company!=null
        company?.apply {
            lblCompanyName.text = name
            lblCompanyStructure.text = structure
            lblCompanyAddress.text = address?.replace(",", "\n")
            setupVisibilityBasedOnContent(lblCompanyName, lblCompanyStructure, lblCompanyAddress)
        }
    }

    private fun setupButtonVisibility(isCompanySelected: Boolean){
        btnContinue.isVisible = isCompanySelected
        btnClear.isVisible = isCompanySelected
        btnFindBusiness.isVisible = !isCompanySelected
        btnSkip.isVisible = !isCompanySelected
    }

}