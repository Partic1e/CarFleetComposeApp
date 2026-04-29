package com.example.domain.usecase.car

import com.example.domain.repository.CarRepository

interface SendToMaintenanceUseCase {
    suspend operator fun invoke(id: String, comment: String): Result<Unit>
}

class SendToMaintenanceUseCaseImpl(
    private val carRepository: CarRepository
) : SendToMaintenanceUseCase {
    override suspend operator fun invoke(id: String, comment: String) = carRepository.sendToMaintenance(id, comment)
}
