package com.backbase.android.flow.businessrelations.model


import com.google.gson.annotations.SerializedName

data class RelationTypeResponseModel(
    @SerializedName("minimumOwnershipPercentage")
    val minimumOwnershipPercentage: Double,
    @SerializedName("relationType")
    val relationType: String
)