package com.backbase.lookup.business_structure.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusinessDetailsResponseModel(
    @SerializedName("address")
    val address: Address,
    @SerializedName("details")
    val details: Details,
    @SerializedName("industries")
    val industries: List<Industry>
): Parcelable