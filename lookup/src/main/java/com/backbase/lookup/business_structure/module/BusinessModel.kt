package com.backbase.lookup.business_structure.module


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusinessModel(
    @SerializedName("address")
    val address: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("structure")
    val structure: String?,
    @SerializedName("jurisdictionCode")
    val jurisdictionCode: String?
): Parcelable