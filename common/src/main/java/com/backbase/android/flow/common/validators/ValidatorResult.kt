package com.backbase.android.flow.common.validators

import android.widget.TextView

class ValidatorResult(val editText: TextView) {
    var observable: ((error: Validator?) -> Unit)? = null
    fun notifyOnChange(error: Validator? = null) {
        observable?.invoke(error)
    }

    fun refresh(){
        editText.setText(editText.text)
    }
}