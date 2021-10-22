package com.backbase.lookup.business_structure.module


import com.google.gson.annotations.SerializedName

data class BusinessStructureResponseModel(
    @SerializedName("performCompanyLookup")
    val performCompanyLookup: Boolean
)