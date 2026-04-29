package com.example.domain.usecase.auth

import com.example.domain.model.auth.AuthResult
import com.example.domain.model.auth.UserRegistration
import com.example.domain.repository.AuthRepository

interface RegisterUseCase {
    suspend operator fun invoke(user: UserRegistration): Result<AuthResult>
}

class RegisterUseCaseImpl(
    private val authRepository: AuthRepository
) : RegisterUseCase {
    override suspend fun invoke(user: UserRegistration): Result<AuthResult> {
        if (user.password.length < 8) {
            return Result.failure(Exception("Пароль слишком короткий"))
        }

        return authRepository.register(user)
    }
}
