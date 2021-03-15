package com.backbase.android.flow.smeo.aboutyou

/**
 * Created by Backbase R&D B.V. on 2010-05-29.
 *
 * Configuration options for the About You screen. Can be created via a DSL in Kotlin or via a [Builder] in Java.
 *
 */
class AboutYouConfiguration private constructor(
    val isOffline: Boolean
) {

    /**
     * A builder for this configuration class.
     *
     * Should be used directly by Java callers. Kotlin callers should use the DSL function instead.
     */
    class Builder {

        @set:JvmSynthetic
        var isOffline: Boolean = false

        fun build() =
            AboutYouConfiguration(isOffline)
    }

}

/**
 * DSL function to create a [AboutYouConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun AboutYouConfiguration(initializer: AboutYouConfiguration.Builder.() -> Unit): AboutYouConfiguration =
    AboutYouConfiguration.Builder().apply(initializer).build()
