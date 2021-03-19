package com.backbase.android.flow.smeo.business.ui

import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.smeo.business.ui.validators.ValidatorResult

class ButtonValidator(var button: BackbaseButton, vararg validatorResults: ValidatorResult) {

    var validatorsMap = HashMap<ValidatorResult, Boolean>()

    init {
        validatorResults.forEach {
            validatorsMap[it] = false
            it.observable = { error ->
                validate(it, error == null)
            }
        }
        checkValidity()
    }

    private fun validate(validatorResult: ValidatorResult, isValid: Boolean) {
        validatorsMap[validatorResult] = isValid
        checkValidity()
    }

    private fun checkValidity(){
        button.isEnabled = validatorsMap.values.firstOrNull { !it } != false
    }

}