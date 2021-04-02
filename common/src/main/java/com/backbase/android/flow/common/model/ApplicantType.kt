package com.backbase.android.flow.common.model

import android.app.Activity
import android.content.Context
import java.io.Serializable

private const val ONBOARDING_APPLICANT_TYPE = "ONBOARDING_APPLICANT_TYPE"

enum class ApplicantType : Serializable {
    SINGLE, COAPPLICANT;

    companion object {
        fun setSelectedApplicantType(context: Context, selectedApplicantType: ApplicantType) {
            val sharedPreferences =
                context.getSharedPreferences(ONBOARDING_APPLICANT_TYPE, Activity.MODE_PRIVATE)
            val edit = sharedPreferences.edit()
            edit.putString(ONBOARDING_APPLICANT_TYPE, selectedApplicantType.toString())
            edit.apply()
        }

        fun getSelectedApplicantType(context: Context): ApplicantType {
            val sharedPreferences =
                context.getSharedPreferences(ONBOARDING_APPLICANT_TYPE, Activity.MODE_PRIVATE)
            val selectedApplicantStr =
                sharedPreferences.getString(ONBOARDING_APPLICANT_TYPE, "")
            return values().find { it.toString() == selectedApplicantStr } ?: SINGLE
        }
    }
}