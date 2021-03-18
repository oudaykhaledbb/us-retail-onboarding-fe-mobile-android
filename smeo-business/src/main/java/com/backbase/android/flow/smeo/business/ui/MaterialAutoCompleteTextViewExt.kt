package com.backbase.android.flow.smeo.business.ui

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView

fun MaterialAutoCompleteTextView.fill(context: Context, data: List<String>){
    this.setAdapter(ArrayAdapter(context, R.layout.simple_list_item_1, data))
}

fun MaterialAutoCompleteTextView.fill(context: Context, vararg data: String){
    this.setAdapter(ArrayAdapter(context, R.layout.simple_list_item_1, data))
}