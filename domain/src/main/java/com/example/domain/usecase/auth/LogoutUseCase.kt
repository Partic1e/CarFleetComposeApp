package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository
) : LogoutUseCase {
    override suspend operator fun invoke() {
        authRepository.logout()
    }
}
