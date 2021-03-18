package com.backbase.android.flow.smeo.business.ui.validators

import android.widget.EditText
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

fun EditText.applyValidations(textInputLayout: TextInputLayout, validators: Map<Validator, String>) {
    val compositeDisposable = CompositeDisposable()
    compositeDisposable.add(this.textChanges().subscribe {
        textInputLayout.error = null
        checkValidity(this.text.toString(), validators.map { it.key }.toList()).validator?.let {
            textInputLayout.error = validators.toMap()[it]
        }
    })
}

fun EditText.applyValidations(
    textInputLayout: TextInputLayout,
    vararg validators: Pair<Validator, String>
) {
    this.applyValidations(textInputLayout, validators.toMap())
}

fun CalendarButton.applyValidations(validators: Map<Validator, String>) {
    val compositeDisposable = CompositeDisposable()
    compositeDisposable.add(this.textChanges().subscribe {
        this.error = null
        checkValidity(this.text.toString(), validators.map { it.key }.toList()).validator?.let {
            this.error = validators.toMap()[it]
        }
    })
}

fun CalendarButton.applyValidations(
    vararg validators: Pair<Validator, String>
) {
    this.applyValidations(validators.toMap())
}

