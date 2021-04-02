package com.backbase.android.flow.common.extensions

import android.content.Context
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView

fun MaterialAutoCompleteTextView.fill(context: Context, data: List<String>){
    this.setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, data))
}
 