package com.backbase.android.flow.uploadfiles.models


import com.google.gson.annotations.SerializedName

data class Registrar(
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("emailAddress")
    val emailAddress: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String
)