package com.example.data.mapper

import com.example.data.model.user.UserInfoDto
import com.example.domain.model.user.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = this.id,
        fullName = this.fullName,
        phoneNumber = this.phoneNumber
    )
}