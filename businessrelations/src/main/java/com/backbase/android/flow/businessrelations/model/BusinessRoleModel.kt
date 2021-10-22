package com.backbase.android.flow.businessrelations.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusinessRoleModel(
    @Json(name = "name")
    @SerializedName("name")
    val name: String
) : Parcelable