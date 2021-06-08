package com.backbase.android.flow.smeo.landing


/**
 * Created by Backbase R&D B.V. on 2021-06-02.
 *
 * Configuration options for the Landing screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class LandingConfiguration private constructor(
    val applicationCenterUrl: String
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        lateinit var applicationCenterUrl: String

        fun build() =
            LandingConfiguration(applicationCenterUrl)
    }

}

/**
 * DSL function to create a [LandingConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun landingConfiguration(initializer: LandingConfiguration.Builder.() -> Unit): LandingConfiguration =
    LandingConfiguration.Builder().apply(initializer).build()
