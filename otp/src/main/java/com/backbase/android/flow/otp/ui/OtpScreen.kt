package com.backbase.android.flow.otp.ui

import com.backbase.android.flow.common.fragment.SecureFragment
import com.backbase.android.flow.common.uicomponents.stepinfo.HeaderInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfo
import com.backbase.android.flow.common.uicomponents.stepinfo.StepInfoPublisher
import com.backbase.android.flow.otp.R
import com.backbase.deferredresources.DeferredText
import org.koin.android.ext.android.inject

const val JOURNEY_NAME_OTP = "JOURNEY_NAME_OTP"

class OtpScreen : SecureFragment(R.layout.screen_otp) {
    private val stepPublisher: StepInfoPublisher by inject()

    override fun onResume() {
        super.onResume()
        stepPublisher.publish(JourneyStepsOTP.OTP.value)
    }

    companion object{
        val JOURNEY_HEADER_INFO_DEFAULT = linkedMapOf(
            JourneyStepsOTP.OTP.value.name to HeaderInfo(
                DeferredText.Resource(R.string.otp_channel_selection_title),
                DeferredText.Resource(R.string.otp_verification_screen_subtitle),
                JourneyStepsOTP.OTP.value.allowBack
            )
        )
    }

}

enum class JourneyStepsOTP(val value: StepInfo) {
    OTP(StepInfo(JOURNEY_NAME_OTP, "OTP", false))
}
