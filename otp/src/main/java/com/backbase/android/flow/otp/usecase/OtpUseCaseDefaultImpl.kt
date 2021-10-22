package com.backbase.android.flow.otp.usecase

import com.backbase.android.flow.common.v2.interaction.OnboardingBaseUseCaseDefaultImpl
import com.backbase.android.flow.common.model.OnboardingModel
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.android.flow.otp.OtpConfiguration
import com.backbase.android.flow.otp.models.EmailModel
import com.backbase.android.flow.otp.models.OtpChannel
import com.backbase.android.flow.otp.models.OtpModel
import com.google.gson.reflect.TypeToken

class OtpUseCaseDefaultImpl(
    private val flowClient: FlowClientContract,
    private val configuration: OtpConfiguration
) : OnboardingBaseUseCaseDefaultImpl(flowClient), OtpUseCase {

    override suspend fun fetchOtpEmail(): String? {
        val result: InteractionResponse<EmailModel>? = flowClient.performInteraction(
            Action(configuration.fetchOtpEmailActionName!!, null),
            EmailModel::class.java
        )
        return result?.body?.email
    }

    override suspend fun requestVerificationCode(recipient: String, otpChannel: OtpChannel): InteractionResponse<OnboardingModel>? {
        val payload = OtpModel(channel = otpChannel.toString())
        when (otpChannel) {
            OtpChannel.EMAIL -> payload.email = recipient
            OtpChannel.SMS -> payload.phoneNumber = recipient
        }
        return submit(configuration.requestActionName, payload)
    }

    override suspend fun submitVerificationCode(recipient: String, otpChannel: OtpChannel, otp: String): InteractionResponse<OnboardingModel>? {
        val payload = OtpModel(channel = otpChannel.toString(), otp = otp)
        when (otpChannel) {
            OtpChannel.EMAIL -> payload.email = recipient
            OtpChannel.SMS -> payload.phoneNumber = recipient
        }
        return submit(configuration.verifyActionName, payload)
    }

    override suspend fun requestAvailableOtpChannels(): List<OtpChannel> {
        val result: InteractionResponse<List<String>>? = flowClient.performInteraction(
            Action(
                configuration.availableOtpChannelsActionName,
                null
            ),
            object : TypeToken<List<String>>() {}.type
        )
        var channels: List<OtpChannel> = listOf()
        result?.body?.let {
            channels = it.map(OtpChannel::valueOf)
        }
        return channels
    }
}


