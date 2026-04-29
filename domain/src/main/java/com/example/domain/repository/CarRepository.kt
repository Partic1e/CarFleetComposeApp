package com.example.domain.repository

import com.example.domain.model.car.Car

interface CarRepository {
    suspend fun getCar(id: String): Result<Car>
    suspend fun updateCarData(car: Car, newMileage: Int): Result<Unit>
    suspend fun sendToMaintenance(id: String, comment: String): Result<Unit>
}
