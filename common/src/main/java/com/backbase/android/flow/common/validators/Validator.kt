package com.backbase.android.flow.common.validators

interface Validator {
    fun validate(input: String?): Boolean
}