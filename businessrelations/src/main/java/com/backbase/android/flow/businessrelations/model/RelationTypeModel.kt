package com.backbase.android.flow.businessrelations.model


import com.google.gson.annotations.SerializedName

data class RelationTypeModel(
    @SerializedName("relationType")
    val relationType: String
)