package com.backbase.lookup.business_info.models

import com.google.gson.annotations.SerializedName

data class BusinessDetailsModel(
    @SerializedName("businessStructureInfo")
    val businessStructureInfo: BusinessStructureInfo,
    @SerializedName("dateEstablished")
    val dateEstablished: String,
    @SerializedName("dba")
    val dba: String,
    @SerializedName("ein")
    val ein: Int?,
    @SerializedName("legalName")
    val legalName: String,
    @SerializedName("stateOperatingIn")
    val stateOperatingIn: String
)