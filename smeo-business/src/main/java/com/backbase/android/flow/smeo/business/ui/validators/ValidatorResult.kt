package com.backbase.android.flow.smeo.business.ui.validators

class ValidatorResult {
    var observable: ((error: Validator?) -> Unit)? = null
    fun notifyOnChange(error: Validator? = null) {
        observable?.invoke(error)
    }
}