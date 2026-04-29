package com.example.domain.usecase.car

import com.example.domain.model.car.Car
import com.example.domain.repository.CarRepository

interface GetCarUseCase {
    suspend operator fun invoke(id: String): Result<Car>
}

class GetCarUseCaseImpl(
    private val carRepository: CarRepository
) : GetCarUseCase {
    override suspend operator fun invoke(id: String) = carRepository.getCar(id)
}
