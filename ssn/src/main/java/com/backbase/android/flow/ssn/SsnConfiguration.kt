package com.backbase.android.flow.ssn

import com.backbase.android.flow.ssn.SsnConfiguration.Builder

/**
 * Created by Backbase R&D B.V. on 2021-05-11.
 *
 * Configuration options for the SSN Journey. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class SsnConfiguration private constructor(
    val isOffline: Boolean,
    val submitSsnActionName: String,
    val landingActionName: String? = null
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
        lateinit var submitSsnActionName: String

        @set:JvmSynthetic
        var landingActionName: String? = null

        fun build() =
            SsnConfiguration(isOffline, submitSsnActionName, landingActionName)
    }

}

/**
 * DSL function to create a [SsnConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun ssnConfiguration(initializer: SsnConfiguration.Builder.() -> Unit): SsnConfiguration =
    SsnConfiguration.Builder().apply(initializer).build()

