package com.example.data.repository

import com.example.data.model.user.UpdateUserRequestDto
import com.example.data.network.UserApi
import com.example.domain.model.user.User
import com.example.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(User(
                    id = dto.id,
                    phoneNumber = dto.phoneNumber,
                    role = dto.role,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    patronymic = dto.patronymic,
                    email = dto.email,
                    post = dto.post,
                    isActive = dto.isActive
                ))
            } else {
                Result.failure(Exception("Ошибка загрузки профиля: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        patronymic: String,
        email: String,
        post: String
    ): Result<Unit> {
        return try {
            val request = UpdateUserRequestDto(firstName, lastName, patronymic, email, post)
            val response = api.updateProfile(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка обновления: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
