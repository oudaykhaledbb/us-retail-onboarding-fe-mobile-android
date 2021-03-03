package com.backbase.android.flow.smeo.walkthrough

class WalkthroughConfiguration private constructor(
    val pages: List<PageContent>
) {
    class Builder {

        @set:JvmSynthetic
        lateinit var pages: List<PageContent>

        fun build() = WalkthroughConfiguration(pages)

    }
}

/**
 * DSL function to create a [WalkthroughConfiguration] in Kotlin.
 */
@JvmSynthetic // Hide from Java callers who should use Builder
fun walkthroughConfiguration(initializer: WalkthroughConfiguration.Builder.() -> Unit): WalkthroughConfiguration =
    WalkthroughConfiguration.Builder().apply(initializer).build()