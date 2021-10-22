package com.backbase.android.flow.otp.usecase

import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.otp.models.OtpChannel
import com.backbase.android.flow.v2.models.InteractionResponse

interface OtpUseCase {
    suspend fun fetchOtpEmail(): String?
    suspend fun requestVerificationCode(recipient: String, otpChannel: OtpChannel): InteractionResponse<OnboardingModel>?
    suspend fun submitVerificationCode(recipient: String, otpChannel: OtpChannel, otp: String): InteractionResponse<OnboardingModel>?
    suspend fun requestAvailableOtpChannels(): List<OtpChannel>
}