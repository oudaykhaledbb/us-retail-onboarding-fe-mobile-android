package com.backbase.android.flow.common.validators

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class ValidatorEmpty : Validator {
    override fun validate(input: String?) = input?.isNotEmpty() ?: false
}

class ValidatorCalendarNotEmpty(private val dateFormat: String = "dd MMM yyyy") : Validator {
    override fun validate(input: String?): Boolean {
        return try{
            val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            sdf.parse(input) // try to parse date
            true
        }catch (ex: Exception){
            false
        }
    }
}

class ValidatorDateOfBirthOver18(private val dateFormat: String = "dd MMM yyyy"): Validator {
    override fun validate(input: String?): Boolean {
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        cal.time = sdf.parse(input)
        return System.currentTimeMillis() - cal.timeInMillis > 568025136000
    }
}

class ValidatorDateOfBirthLess99(private val dateFormat: String = "dd MMM yyyy"): Validator {
    override fun validate(input: String?): Boolean {
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        cal.time = sdf.parse(input)
        return System.currentTimeMillis() - cal.timeInMillis < 3124138248000
    }
}

class ValidatorEmail : Validator {
    override fun validate(input: String?): Boolean {
        val pattern: Pattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", Pattern.CASE_INSENSITIVE)
        return pattern.matcher(input).matches()
    }
}

class ValidatorMaxChar(private val maxLength: Int) : Validator {
    override fun validate(input: String?): Boolean {
        return if (input.isNullOrEmpty()) true else input.toString().length <= maxLength
    }
}

class ValidatorMinChar(private val minLength: Int) : Validator {
    override fun validate(input: String?): Boolean {
        return if (input.isNullOrEmpty()) true else input.toString().length >= minLength
    }
}

class ValidatorCharLength(private val minLength: Int) : Validator {
    override fun validate(input: String?): Boolean {
        return if (input.isNullOrEmpty()) true else input.toString().length == minLength
    }
}