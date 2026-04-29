package com.example.domain.model.user

data class User(
    val id: String,
    val phoneNumber: String,
    val role: Int,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val email: String?,
    val post: String,
    val isActive: Boolean
)
