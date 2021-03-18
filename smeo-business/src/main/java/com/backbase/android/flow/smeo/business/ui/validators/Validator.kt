package com.backbase.android.flow.smeo.business.ui.validators

interface Validator {
    fun validate(input: String?): Boolean
}