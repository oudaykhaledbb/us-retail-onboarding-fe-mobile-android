package com.backbase.android.flow.businessrelations.model

import android.os.Parcelable
import com.backbase.android.flow.businessrelations.usecase.DEFAULT_ROLE
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(
    @Json(name = "id")
    @SerializedName("id")
    var id: String? = null,
    @Json(name = "firstName")
    @SerializedName("firstName")
    var firstName: String = "",
    @Json(name = "lastName")
    @SerializedName("lastName")
    var lastName: String = "",
    @Json(name = "email")
    @SerializedName("email")
    var email: String = "",
    @Json(name = "phone")
    @SerializedName("phone")
    var phone: String? = "",
    @Json(name = "role")
    @SerializedName("role")
    var role: String? = "",
    @Json(name = "otherRole")
    @SerializedName("otherRole")
    var otherRole: String? = "",
    @Json(name = "ownershipPercentage")
    @SerializedName("ownershipPercentage")
    var ownershipPercentage: Int? = 0,
    @Json(name = "relationType")
    @SerializedName("relationType")
    var relationTypeStr: String = RelationType.CONTROL_PERSON.toString(),
    @Json(name = "currentUser")
    @SerializedName("currentUser")
    var isCurrentUser: Boolean? = false,
    @Json(name = "controlPerson")
    @SerializedName("controlPerson")
    var controlPerson: Boolean = false
) : Parcelable

fun Owner.relationType() =
        if ("CONTROL_PERSON" == this.relationTypeStr)
            RelationType.CONTROL_PERSON
        else RelationType.OWNER

fun Owner.fullName() = "$firstName $lastName"
fun Owner.findRole() =
    if ("${role?.toLowerCase()}".contentEquals(DEFAULT_ROLE)) otherRole else role