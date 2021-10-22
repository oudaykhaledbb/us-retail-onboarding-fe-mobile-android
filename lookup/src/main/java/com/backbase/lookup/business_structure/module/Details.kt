package com.backbase.lookup.business_structure.module


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Details(
    @SerializedName("companyType")
    val companyType: String,
    @SerializedName("dateEstablished")
    val dateEstablished: String,
    @SerializedName("ein")
    val ein: String,
    @SerializedName("legalName")
    val legalName: String,
    @SerializedName("stateOperatingIn")
    val stateOperatingIn: String
): Parcelable