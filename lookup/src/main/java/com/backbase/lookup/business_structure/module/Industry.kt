package com.backbase.lookup.business_structure.module


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Industry(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String
) : Parcelable