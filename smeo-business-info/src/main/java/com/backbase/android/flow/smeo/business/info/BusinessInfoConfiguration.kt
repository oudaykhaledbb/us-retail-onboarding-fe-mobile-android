package com.backbase.android.flow.smeo.business.info


/**
 * Created by Backbase R&D B.V. on 2021-03-03.
 *
 * Configuration options for the Business Info Journey. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class BusinessInfoConfiguration private constructor(
    val isOffline: Boolean,
    val submitBusinessDetailsAction: String
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
        lateinit var submitBusinessDetailsAction: String

        fun build() =
            BusinessInfoConfiguration(isOffline, submitBusinessDetailsAction)
    }

}

/**
 * DSL function to create a [AboutYouConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun businessInfoConfiguration(initializer: BusinessInfoConfiguration.Builder.() -> Unit): BusinessInfoConfiguration =
    BusinessInfoConfiguration.Builder().apply(initializer).build()
