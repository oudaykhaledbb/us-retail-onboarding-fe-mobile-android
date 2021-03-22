package com.backbase.android.flow.smeo.business


/**
 * Created by Backbase R&D B.V. on 2021-03-03.
 *
 * Configuration options for the Business Journey. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class BusinessConfiguration private constructor(
    val isOffline: Boolean,
    val verifyCaseAction: String,
    val submitBusinessDetailsAction: String,
    val submitBusinessIdentityAction: String,
    val submitBusinessAddressAction: String,
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        var isOffline: Boolean = false

        @set:JvmSynthetic
        lateinit var verifyCaseAction: String

        @set:JvmSynthetic
        lateinit var submitBusinessDetailsAction: String

        @set:JvmSynthetic
        lateinit var submitBusinessIdentityAction: String

        @set:JvmSynthetic
        lateinit var submitBusinessAddressAction: String

        fun build() =
            BusinessConfiguration(isOffline, verifyCaseAction, submitBusinessDetailsAction, submitBusinessIdentityAction, submitBusinessAddressAction)
    }

}

/**
 * DSL function to create a [AboutYouConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun BusinessConfiguration(initializer: BusinessConfiguration.Builder.() -> Unit): BusinessConfiguration =
    BusinessConfiguration.Builder().apply(initializer).build()
