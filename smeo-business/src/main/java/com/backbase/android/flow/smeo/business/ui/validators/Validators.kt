package com.backbase.android.flow.smeo.business.ui.validators

import android.util.Patterns
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ValidatorEmpty : Validator {
    override fun validate(input: String?) = input?.isNotEmpty() ?: false
}

class ValidatorEmail : Validator {
    override fun validate(input: String?) =
        input?.isNullOrEmpty() ?: false && Patterns.EMAIL_ADDRESS.matcher(input).matches()
}

class ValidatorCalendarNotEmpty : Validator {
    override fun validate(input: String?): Boolean {
        return try{
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            sdf.parse(input) // try to parse date
            true
        }catch (ex: Exception){
            false
        }
    }
}

class ValidatorDateOfBirthOver18: Validator{
    override fun validate(input: String?): Boolean {
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        cal.time = sdf.parse(input)
        return cal.timeInMillis - System.currentTimeMillis() > 568025136000
    }

}