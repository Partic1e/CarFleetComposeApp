package com.example.domain.model.auth

data class AuthResult(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSeconds: Int,
    val role: Int
)
