package com.example.domain.repository

import com.example.domain.model.auth.AuthResult
import com.example.domain.model.auth.UserLogin
import com.example.domain.model.auth.UserRegistration

interface AuthRepository {
    suspend fun login(user: UserLogin): Result<AuthResult>
    suspend fun register(user: UserRegistration): Result<AuthResult>
    suspend fun logout()
}
