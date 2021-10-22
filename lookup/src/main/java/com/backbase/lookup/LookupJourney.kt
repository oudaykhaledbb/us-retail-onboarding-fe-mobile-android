package com.backbase.lookup

import androidx.fragment.app.Fragment
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.deferredresources.DeferredText

class LookupJourney : Fragment(R.layout.journey_lookup){
    companion object {
        val HEADER_INFO_DEFAULT = hashMapOf(
            JourneyStepsLookup.LOOKUP_ADDRESS.value.name to HeaderInfo(
                DeferredText.Resource(R.string.lookup_journey_title_lookup_address),
                DeferredText.Resource(R.string.lookup_journey_subtitle_lookup_address),
                JourneyStepsLookup.LOOKUP_ADDRESS.value.allowBack
            ),
            JourneyStepsLookup.BUSINESS_IDENTITY.value.name to HeaderInfo(
                DeferredText.Resource(R.string.lookup_journey_title_business_identity),
                DeferredText.Resource(R.string.lookup_journey_subtitle_business_identity),
                JourneyStepsLookup.BUSINESS_IDENTITY.value.allowBack
            ),
            JourneyStepsLookup.BUSINESS_INFO.value.name to HeaderInfo(
                DeferredText.Resource(R.string.lookup_journey_title_business_info),
                DeferredText.Resource(R.string.lookup_journey_subtitle_business_info),
                JourneyStepsLookup.BUSINESS_INFO.value.allowBack
            ),
            JourneyStepsLookup.COMPANY_LOOKUP.value.name to HeaderInfo(
                DeferredText.Resource(R.string.lookup_journey_title_company_lookup),
                DeferredText.Resource(R.string.lookup_journey_subtitle_company_lookup),
                JourneyStepsLookup.COMPANY_LOOKUP.value.allowBack
            ),
            JourneyStepsLookup.BUSINESS_STRUCTURE.value.name to HeaderInfo(
                DeferredText.Resource(R.string.lookup_journey_title_business_structure),
                DeferredText.Resource(R.string.lookup_journey_subtitle_business_structure),
                JourneyStepsLookup.BUSINESS_STRUCTURE.value.allowBack
            )
        )
    }

    enum class JourneyStepsLookup(val value: StepInfo){
        LOOKUP_ADDRESS(StepInfo(JOURNEY_NAME_LOOKUP_JOURNEY,  "LOOKUP_ADDRESS", false)),
        BUSINESS_IDENTITY(StepInfo(JOURNEY_NAME_LOOKUP_JOURNEY,  "BUSINESS_IDENTITY", false)),
        BUSINESS_INFO(StepInfo(JOURNEY_NAME_LOOKUP_JOURNEY,  "BUSINESS_INFO", false)),
        COMPANY_LOOKUP(StepInfo(JOURNEY_NAME_LOOKUP_JOURNEY,  "COMPANY_LOOKUP", false)),
        BUSINESS_STRUCTURE(StepInfo(JOURNEY_NAME_LOOKUP_JOURNEY,  "BUSINESS_STRUCTURE", false))
    }

}

const val JOURNEY_NAME_LOOKUP_JOURNEY= "JOURNEY_NAME_LOOKUP_JOURNEY"



