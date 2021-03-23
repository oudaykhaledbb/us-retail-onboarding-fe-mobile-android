package com.backbase.android.flow.smeo.aboutyou

/**
 * Created by Backbase R&D B.V. on 2010-05-29.
 *
 * Configuration options for the About You screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class AboutYouConfiguration private constructor(
    val isOffline: Boolean,
    val actionInit: String,
    val actionAboutYou: String
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
        lateinit var actionInit: String

        @set:JvmSynthetic
        lateinit var actionAboutYou: String

        fun build() =
            AboutYouConfiguration(isOffline, actionInit, actionAboutYou)
    }

}

/**
 * DSL function to create a [AboutYouConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun AboutYouConfiguration(initializer: AboutYouConfiguration.Builder.() -> Unit): AboutYouConfiguration =
    AboutYouConfiguration.Builder().apply(initializer).build()
