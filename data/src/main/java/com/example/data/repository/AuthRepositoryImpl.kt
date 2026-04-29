package com.example.data.repository

import com.example.data.local.TokenStorage
import com.example.data.mapper.toDomain
import com.example.data.mapper.toDto
import com.example.data.model.auth.LoginRequestDto
import com.example.data.network.AuthApi
import com.example.domain.model.auth.AuthResult
import com.example.domain.model.auth.UserLogin
import com.example.domain.model.auth.UserRegistration
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(user: UserLogin): Result<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequestDto(
                    phoneNumber = user.phoneNumber,
                    password = user.password
                )
                val response = api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.toDomain())
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Ошибка авторизации"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun register(user: UserRegistration): Result<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val request = user.toDto()
                val response = api.register(request)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.toDomain())
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Ошибка при регистрации"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                api.logout()
            } catch (e: Exception) {

            } finally {
                tokenStorage.clearTokens()
            }
        }
    }
}
