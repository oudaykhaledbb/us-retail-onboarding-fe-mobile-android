package com.backbase.android.flow.common.validators

import android.widget.EditText
import android.widget.TextView
import com.backbase.android.design.calendar.CalendarButton
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable

data class ValidationResult(
    val isValid: Boolean,
    val validator: Validator? = null
)

fun checkValidity(input: String, validators: List<Validator>): ValidationResult {
    validators.forEach { validator ->
        if (!validator.validate(input)) return ValidationResult(false, validator)
    }
    return ValidationResult(true)
}

fun checkValidity(input: String, vararg validators: Validator) =
    checkValidity(input, validators.toList())

fun EditText.applyValidations(
    textInputLayout: TextInputLayout,
    validators: Map<Validator, String>
): ValidatorResult {
    val compositeDisposable = CompositeDisposable()
    val validatorResult = ValidatorResult(this)
    var isFirstTime = false
    this.tag = ValidationHolder(textInputLayout, validators)
    compositeDisposable.add(this.textChanges().subscribe {
        textInputLayout.error = null
        validatorResult.notifyOnChange()
        checkValidity(this.text.toString(), validators.map { it.key }.toList()).validator?.let {
            if (isFirstTime) {
                isFirstTime = false
            } else {
                textInputLayout.error = validators.toMap()[it]
            }
            validatorResult.notifyOnChange(it)
        }
    })
    return validatorResult
}

fun EditText.applyValidations(
    textInputLayout: TextInputLayout,
    vararg validators: Pair<Validator, String>
) = this.applyValidations(textInputLayout, validators.toMap())

fun CalendarButton.applyValidations(
    txtCalendarHelperText: TextView,
    validators: Map<Validator, String>
): ValidatorResult {
    val compositeDisposable = CompositeDisposable()
    val validatorResult = ValidatorResult(this)
    var isFirstTime = false
    this.tag = ValidationCalendarHolder(txtCalendarHelperText, validators)
    compositeDisposable.add(this.textChanges().subscribe {
        this.error = null
        txtCalendarHelperText.text = ""
        validatorResult.notifyOnChange()
        checkValidity(this.text.toString(), validators.map { it.key }.toList()).validator?.let {
            if (isFirstTime) {
                isFirstTime = false
            } else {
                this.error = validators.toMap()[it]
                txtCalendarHelperText.text = validators.toMap()[it]
            }
            validatorResult.notifyOnChange(it)
        }
    })
    return validatorResult
}

fun CalendarButton.applyValidations(
    txtCalendarHelperText: TextView,
    vararg validators: Pair<Validator, String>
) = this.applyValidations(txtCalendarHelperText, validators.toMap())

data class ValidationHolder(
    val textInputLayout: TextInputLayout,
    val validators: Map<Validator, String>)

data class ValidationCalendarHolder(
    val textInputLayout: TextView,
    val validators: Map<Validator, String>)