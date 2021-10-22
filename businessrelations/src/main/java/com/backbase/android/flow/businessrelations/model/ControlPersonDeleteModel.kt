package com.backbase.android.flow.businessrelations.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ControlPersonDeleteModel (
    @Json(name = "id")
    val id: String
): Parcelable