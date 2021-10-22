package com.backbase.android.flow.identityverification

import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.deferredresources.DeferredText
import org.koin.android.ext.android.inject

const val JOURNEY_NAME_IDENTITY_VERIFICATION = "JOURNEY_NAME_IDENTITY_VERIFICATION"

class IdentityVerificationScreen : SecureFragment(R.layout.screen_idv){

    private val stepPublisher: StepInfoPublisher by inject()

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsIdentityVerification.IDENTITY_VERIFICATION.value)
    }

    companion object{
        val JOURNEY_HEADER_INFO_DEFAULT = linkedMapOf(
            JourneyStepsIdentityVerification.IDENTITY_VERIFICATION.value.name to HeaderInfo(
                DeferredText.Resource(R.string.identity_verification_title),
                DeferredText.Resource(R.string.identity_verification_subtitle),
                JourneyStepsIdentityVerification.IDENTITY_VERIFICATION.value.allowBack
            )
        )
    }
}

enum class JourneyStepsIdentityVerification(val value: StepInfo) {
    IDENTITY_VERIFICATION(StepInfo(JOURNEY_NAME_IDENTITY_VERIFICATION, "IDENTITY_VERIFICATION", false))
}
