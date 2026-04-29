package com.example.domain.usecase.auth

import com.example.domain.model.auth.AuthResult
import com.example.domain.model.auth.UserLogin
import com.example.domain.repository.AuthRepository

interface LoginUseCase {
    suspend operator fun invoke(user: UserLogin): Result<AuthResult>
}

class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase {
    override suspend operator fun invoke(user: UserLogin): Result<AuthResult> {
        if (user.phoneNumber.isBlank() || user.password.length < 8)
            return Result.failure(Exception("Неверные входные данные"))

        return authRepository.login(user)
    }
}
