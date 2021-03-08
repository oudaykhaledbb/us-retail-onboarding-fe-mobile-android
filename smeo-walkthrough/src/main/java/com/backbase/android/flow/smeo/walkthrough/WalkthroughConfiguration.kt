package com.backbase.android.flow.smeo.walkthrough

import com.backbase.android.flow.smeo.walkthrough.ui.PageContent

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