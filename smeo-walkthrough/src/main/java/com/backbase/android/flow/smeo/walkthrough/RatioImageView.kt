package com.backbase.android.flow.smeo.walkthrough

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView

class RatioImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d: Drawable? = drawable
        if (d != null) {
            val w = MeasureSpec.getSize(widthMeasureSpec)
            val h: Int = w * d.intrinsicHeight / d.intrinsicWidth
            setMeasuredDimension(w, h)
        } else super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}