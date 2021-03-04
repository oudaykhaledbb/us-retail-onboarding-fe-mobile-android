package com.backbase.android.flow.smeo.walkthrough.ui

import com.backbase.android.flow.smeo.walkthrough.models.*
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText
import java.io.Serializable

class WalkthroughPageBuilder {

    private val walkthroughItems = ArrayList<WalkthroughModel>()
    private lateinit var headerImage: DeferredDrawable

    fun setHeaderImage(headerImage: DeferredDrawable): WalkthroughPageBuilder {
        this.headerImage = headerImage
        return this
    }

    fun addHeader(text: DeferredText): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughHeader(
                        text
                )
        )
        return this
    }

    fun addContent(text: DeferredText): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughContent(
                        text
                )
        )
        return this
    }

    fun addSupport(text: DeferredText): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughSupport(
                        text
                )
        )
        return this
    }

    fun addCaption(text: DeferredText): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughCaption(
                        text
                )
        )
        return this
    }

    fun addHeaderWithIcon(icon: DeferredDrawable, text: DeferredText): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughIconHeader(
                        icon,
                        text
                )
        )
        return this
    }

    fun addLabelWithIcon(icon: DeferredDrawable, text: DeferredText): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughIconLabel(
                        icon,
                        text
                )
        )
        return this
    }

    fun addSection(
        icon: DeferredDrawable,
        title: DeferredText,
        subtitle: DeferredText
    ): WalkthroughPageBuilder {
        walkthroughItems.add(
                WalkthroughSection(
                        icon,
                        title,
                        subtitle
                )
        )
        return this
    }

    fun build() = PageContent(
            headerImage,
            walkthroughItems
    )

}

data class PageContent(
    val headerImage: DeferredDrawable,
    val walkthroughItems: ArrayList<WalkthroughModel>
) : Serializable