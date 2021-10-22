package com.backbase.android.flow.businessrelations.ui.screen

import androidx.fragment.app.Fragment
import com.backbase.android.flow.businessrelations.R
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.deferredresources.DeferredText

const val JOURNEY_NAME_BUSINESS_RELATIONS = "JOURNEY_NAME_BUSINESS_RELATIONS"

class BusinessRelationsJourneyScreen : Fragment(R.layout.screen_business_relations_journey){

    companion object{
        val JOURNEY_HEADER_INFO_DEFAULT = linkedMapOf(
            JourneyStepsBusinessRelations.Add_BUSINESS_OWNER.value.name to HeaderInfo(
                DeferredText.Resource(R.string.business_relations_title_add_business_owner),
                DeferredText.Resource(R.string.business_relations_subtitle_add_business_owner),
                JourneyStepsBusinessRelations.Add_BUSINESS_OWNER.value.allowBack
            ),
            JourneyStepsBusinessRelations.ROLE_SELECTION.value.name to HeaderInfo(
                DeferredText.Resource(R.string.business_relations_title_role_selection),
                DeferredText.Resource(R.string.business_relations_subtitle_role_selection),
                JourneyStepsBusinessRelations.ROLE_SELECTION.value.allowBack
            ),
            JourneyStepsBusinessRelations.SELECT_CONTROL_PERSON.value.name to HeaderInfo(
                DeferredText.Resource(R.string.business_relations_title_select_control_person),
                DeferredText.Resource(R.string.business_relations_subtitle_select_control_person),
                JourneyStepsBusinessRelations.SELECT_CONTROL_PERSON.value.allowBack
            ),
            JourneyStepsBusinessRelations.SUMMARY.value.name to HeaderInfo(
                DeferredText.Resource(R.string.business_relations_title_summary),
                DeferredText.Resource(R.string.business_relations_journey_subtitle_summary),
                JourneyStepsBusinessRelations.SUMMARY.value.allowBack
            )
        )
    }
}

enum class JourneyStepsBusinessRelations(val value: StepInfo) {
    Add_BUSINESS_OWNER(StepInfo(JOURNEY_NAME_BUSINESS_RELATIONS, "Add_BUSINESS_OWNER", false)),
    ROLE_SELECTION(StepInfo(JOURNEY_NAME_BUSINESS_RELATIONS, "ROLE_SELECTION", false)),
    SELECT_CONTROL_PERSON(
        StepInfo(
            JOURNEY_NAME_BUSINESS_RELATIONS,
            "SELECT_CONTROL_PERSON",
            false
        )
    ),
    SUMMARY(StepInfo(JOURNEY_NAME_BUSINESS_RELATIONS, "SUMMARY", false))
}

