package com.example.data.mapper

import com.example.data.model.auth.AuthResponseDto
import com.example.data.model.auth.RegisterRequestDto
import com.example.domain.model.auth.AuthResult
import com.example.domain.model.auth.UserRegistration

fun AuthResponseDto.toDomain(): AuthResult {
    return AuthResult(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        expiresInSeconds = this.expiresInSeconds,
        role = this.userRole
    )
}

fun UserRegistration.toDto(): RegisterRequestDto {
    return RegisterRequestDto(
        phoneNumber = this.phoneNumber,
        password = this.password,
        userRole = this.role,
        post = this.post,
        firstName = this.firstName,
        lastName = this.lastName,
        patronymic = this.patronymic,
        email = this.email
    )
}
