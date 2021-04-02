package com.backbase.android.flow.smeo.aboutyou.ui

import com.backbase.android.design.button.BackbaseButton
import com.backbase.android.flow.common.validators.ValidatorResult

class ButtonValidator(val button: BackbaseButton, vararg validatorResults: ValidatorResult) {

    var validatorsMap = HashMap<ValidatorResult, Boolean>()

    init {
        validatorResults.forEach {
            validatorsMap[it] = false
            it.observable = { error ->
                validate(it, error == null)
            }
        }
        checkValidity()
        validatorResults.forEach { it.refresh() }
    }

    private fun validate(validatorResult: ValidatorResult, isValid: Boolean) {
        validatorsMap[validatorResult] = isValid
        checkValidity()
    }

    private fun checkValidity() {
        button.isEnabled = isValid()
    }

    fun isValid() = validatorsMap.values.firstOrNull { !it } != false

}