package com.example.domain.usecase.car

import com.example.domain.model.car.Car
import com.example.domain.repository.CarRepository

interface UpdateCarDataUseCase {
    suspend operator fun invoke(car: Car, newMileage: Int): Result<Unit>
}

class UpdateCarDataUseCaseImpl(
    private val carRepository: CarRepository
) : UpdateCarDataUseCase {
    override suspend operator fun invoke(car: Car, newMileage: Int) = carRepository.updateCarData(car, newMileage)
}
