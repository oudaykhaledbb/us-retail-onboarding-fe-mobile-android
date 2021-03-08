package com.backbase.android.flow.smeo.walkthrough.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.backbase.android.flow.smeo.walkthrough.R
import com.backbase.android.flow.smeo.walkthrough.ui.*
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText
import kotlinx.android.synthetic.main.walkthrough_section.view.*


interface WalkthroughModel {
    fun resolve(context: Context): View
}

class WalkthroughHeader(private val headerText: DeferredText) :
        WalkthroughModel {
    override fun resolve(context: Context) =
        WalkthroughHeaderView(context)
            .apply { text = headerText.resolve(context) }
}

class WalkthroughContent(private val contentText: DeferredText) :
        WalkthroughModel {
    override fun resolve(context: Context) =
        WalkthroughContentView(context)
            .apply { text = contentText.resolve(context) }
}

class WalkthroughSupport(private val supportText: DeferredText) :
        WalkthroughModel {
    override fun resolve(context: Context) =
        WalkthroughSupportView(context)
            .apply { text = supportText.resolve(context) }
}

class WalkthroughCaption(private val captionText: DeferredText) :
        WalkthroughModel {
    override fun resolve(context: Context) =
        WalkthroughCaptionView(context)
            .apply { text = captionText.resolve(context) }
}

class WalkthroughIconHeader(val icon: DeferredDrawable, private val headerText: DeferredText) :
        WalkthroughModel {
    override fun resolve(context: Context) =
        WalkthroughHeaderWithIcon(
                context
        ).apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(icon.resolve(context), null, null, null)
            text = headerText.resolve(context)
        }
}

class WalkthroughIconLabel(val icon: DeferredDrawable, private val labelText: DeferredText) :
        WalkthroughModel {
    override fun resolve(context: Context) =
        WalkthroughLabelWithIcon(
                context
        ).apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(icon.resolve(context), null, null, null)
            text = labelText.resolve(context)
        }
}

class WalkthroughSection(
    private val icon: DeferredDrawable,
    private val title: DeferredText,
    private val subtitle: DeferredText
) : WalkthroughModel {

    override fun resolve(context: Context): View =
        LayoutInflater.from(context).inflate(R.layout.walkthrough_section, null).apply {
            imgIcon.setImageDrawable(icon.resolve(context))
            lblTitle.text = title.resolve(context)
            lblSubTitle.text = subtitle.resolve(context)
        }

}

