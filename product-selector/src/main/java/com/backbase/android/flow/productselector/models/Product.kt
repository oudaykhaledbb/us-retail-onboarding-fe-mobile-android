package com.backbase.android.flow.productselector.models

import android.os.Parcelable
import com.backbase.android.flow.productselector.models.Cost
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val benefits: List<String>,
    val cost: Cost,
    val costsFrequency: String,
    val description: String,
    val detailedProductDescriptionUrl: String,
    val imageUrl: String,
    val name: String,
    val productType: String,
    val referenceId: String
): Parcelable