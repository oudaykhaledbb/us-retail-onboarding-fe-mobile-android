package com.backbase.android.flow.productselector.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cost(
    val currency: String,
    val value: Double
): Parcelable