package com.example.domain.usecase.user

import com.example.domain.repository.UserRepository

interface UpdateProfileUseCase {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        patronymic: String,
        email: String,
        post: String
    ): Result<Unit>
}

class UpdateProfileUseCaseImpl(
    private val userRepository: UserRepository
) : UpdateProfileUseCase {
    override suspend operator fun invoke(
        firstName: String,
        lastName: String,
        patronymic: String,
        email: String,
        post: String
    ): Result<Unit> = userRepository.updateProfile(firstName, lastName, patronymic, email, post)
}
