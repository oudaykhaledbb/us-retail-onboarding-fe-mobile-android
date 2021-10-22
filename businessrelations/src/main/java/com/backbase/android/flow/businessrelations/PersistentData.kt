package com.backbase.android.flow.businessrelations

import com.backbase.android.flow.businessrelations.model.BusinessRoleModel
import com.backbase.android.flow.businessrelations.model.Owner

object PersistentData {
    var currentUser: Owner? = null
    var isCurrentUserTheControlPerson = false
    var roles: List<BusinessRoleModel>? = null
    var msg = ""
}