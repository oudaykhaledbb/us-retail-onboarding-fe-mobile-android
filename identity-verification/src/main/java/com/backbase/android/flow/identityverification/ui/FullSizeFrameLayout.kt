package com.backbase.android.flow.identityverification.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

class FullSizeFrameLayout : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecs = heightMeasureSpec
        if (MeasureSpec.getMode(heightMeasureSpecs) == MeasureSpec.AT_MOST) {
            heightMeasureSpecs = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec),
                MeasureSpec.EXACTLY
            )
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecs)
    }

}