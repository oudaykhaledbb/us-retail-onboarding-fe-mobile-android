package com.backbase.android.flow.businessrelations

import com.backbase.android.flow.businessrelations.model.UserInfo

interface UserInfoProvider {
    fun getUserInfo(): UserInfo
}