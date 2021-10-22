package com.backbase.android.flow.otp

import com.backbase.android.flow.otp.OtpConfiguration.Builder

/**
 * Created by Backbase R&D B.V. on 2010-05-29.
 *
 * Configuration options for the OTP screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class OtpConfiguration private constructor(
    var requestActionName: String,
    var verifyActionName: String,
    var availableOtpChannelsActionName: String,
    var fetchOtpEmailActionName: String? = null,
    var verificationCodeMaxLength: Integer = Integer(6),
    var phoneVerificationPattern: String = "^\\+(?:[0-9] ?){6,14}[0-9]$"
    //international phone numbers based on ITU-T standards
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        lateinit var requestActionName: String

        @set:JvmSynthetic
        lateinit var verifyActionName: String

        @set:JvmSynthetic
        lateinit var availableOtpChannelsActionName: String

        @set:JvmSynthetic
        lateinit var verificationCodeMaxLength: Integer

        @set:JvmSynthetic
        var fetchOtpEmailActionName: String? = null

        fun build() =
            OtpConfiguration(
                requestActionName = requestActionName,
                verifyActionName = verifyActionName,
                availableOtpChannelsActionName = availableOtpChannelsActionName,
                fetchOtpEmailActionName = fetchOtpEmailActionName,
                verificationCodeMaxLength = verificationCodeMaxLength
            )
    }

}

/**
 * DSL function to create a [OtpConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun OtpConfiguration(initializer: Builder.() -> Unit): OtpConfiguration =
    Builder().apply(initializer).build()


