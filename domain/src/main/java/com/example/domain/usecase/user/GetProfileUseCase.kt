package com.example.domain.usecase.user

import com.example.domain.model.user.User
import com.example.domain.repository.UserRepository

interface GetProfileUseCase {
    suspend operator fun invoke(): Result<User>
}

class GetProfileUseCaseImpl(
    private val userRepository: UserRepository
) : GetProfileUseCase {
    override suspend operator fun invoke(): Result<User> = userRepository.getProfile()
}
