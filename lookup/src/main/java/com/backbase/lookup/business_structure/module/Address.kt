package com.backbase.lookup.business_structure.module


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @SerializedName("city")
    val city: String,
    @SerializedName("numberAndStreet")
    val numberAndStreet: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zipCode")
    val zipCode: String
): Parcelable