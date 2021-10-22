package com.backbase.android.flow.productselector.screen.uicomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.backbase.android.flow.productselector.R
import kotlinx.android.synthetic.main.row_product.view.*

class ProductFeaturesView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    fun clearFeatures(){
        removeAllViews()
    }

    fun addFeatures(lstFeatures: List<String>){
        clearFeatures()
        lstFeatures.forEach { addFeature(it) }
    }

    private fun addFeature(feature: String) {
        val view = View.inflate(context, R.layout.view_product_features, null)
        view.lblDescription.text = feature
        addView(view)
    }
}