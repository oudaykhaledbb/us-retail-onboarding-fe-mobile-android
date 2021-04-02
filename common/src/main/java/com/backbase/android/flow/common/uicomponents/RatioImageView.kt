package com.backbase.android.flow.common.uicomponents

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RatioImageView: AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d: Drawable? = drawable
        if (d != null) {
            val w = MeasureSpec.getSize(widthMeasureSpec)
            val h: Int = w * d.intrinsicHeight / d.intrinsicWidth
            setMeasuredDimension(w, h)
        } else super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}