package com.example.domain.model.auth

data class UserRegistration(
    val phoneNumber: String,
    val password: String,
    val role: Int,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val email: String?,
    val post: String
)
