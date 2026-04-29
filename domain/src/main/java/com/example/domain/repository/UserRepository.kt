package com.example.domain.repository

import com.example.domain.model.user.User

interface UserRepository {
    suspend fun getProfile(): Result<User>

    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        patronymic: String,
        email: String,
        post: String
    ): Result<Unit>
}
