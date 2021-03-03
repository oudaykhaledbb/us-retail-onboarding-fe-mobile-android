package com.backbase.android.flow.smeo.walkthrough

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.view.ContextThemeWrapper
import com.backbase.android.flow.smeo.walkthrough.R
import com.google.android.material.textview.MaterialTextView

class WalkthroughHeaderView constructor(context: Context) : MaterialTextView(ContextThemeWrapper(context, R.style.walkthroughHeaderStyle), null, 0)

class WalkthroughContentView constructor(context: Context) : MaterialTextView(ContextThemeWrapper(context, R.style.walkthroughContentStyle), null, 0)

class WalkthroughSupportView constructor(context: Context) : MaterialTextView(ContextThemeWrapper(context, R.style.walkthroughSupportStyle), null, 0)

class WalkthroughCaptionView constructor(context: Context) : MaterialTextView(ContextThemeWrapper(context, R.style.walkthroughCaptionStyle), null, 0)

class WalkthroughHeaderWithIcon constructor(context: Context) : MaterialTextView(ContextThemeWrapper(context, R.style.walkthroughHeaderWithIconStyle), null, 0)

class WalkthroughLabelWithIcon constructor(context: Context) : MaterialTextView(ContextThemeWrapper(context, R.style.walkthroughLabelWithIconStyle), null, 0)



//data class WalkthroughHeader(val text: DeferredText)
//data class WalkthroughContent(val text: DeferredText)
//data class WalkthroughSupport(val text: DeferredText)
//data class WalkthroughCaption(val text: DeferredText)
//data class WalkthroughIconHeader(val icon: DeferredDrawable, val text: DeferredText)
//data class WalkthroughIconLabel(val icon: DeferredDrawable, val text: DeferredText)
//data class WalkthroughSection(val icon: DeferredDrawable, val title: DeferredText, val subtitle: DeferredText)
