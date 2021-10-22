package com.backbase.lookup

import android.app.Activity
import android.content.Context
import com.backbase.lookup.address.models.AddressModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val Address_Shared_Pref = "Address_Shared_Pref"

class LocalStorage(val context: Context) {

    fun storeAddressModel(prefillAddress: AddressModel) {
        context.getSharedPreferences(Address_Shared_Pref, Activity.MODE_PRIVATE).edit()
            .putString(Address_Shared_Pref, Gson().toJson(prefillAddress)).apply()
    }

    fun getAddressModel(): AddressModel =
        Gson().fromJson(context.getSharedPreferences(Address_Shared_Pref, Activity.MODE_PRIVATE)
            .getString(Address_Shared_Pref, null), object : TypeToken<AddressModel?>() {}.type
        )

}