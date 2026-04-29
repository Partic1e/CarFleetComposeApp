package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.mapper.toRequestDto
import com.example.data.model.car.SetMaintenanceRequestDto
import com.example.data.network.CarApi
import com.example.domain.model.car.Car
import com.example.domain.repository.CarRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CarRepositoryImpl(private val api: CarApi) : CarRepository {

    override suspend fun getCar(id: String): Result<Car> {
        return try {
            val response = api.getCarById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Ошибка загрузки авто"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCarData(car: Car, newMileage: Int): Result<Unit> {
        return try {
            val request = car.toRequestDto(newMileage = newMileage)

            val response = api.updateCar(car.id, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Сервер вернул ошибку: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendToMaintenance(id: String, comment: String): Result<Unit> {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val request = SetMaintenanceRequestDto(
                from = sdf.format(Date()),
                to = null,
                comment = comment
            )
            val response = api.setMaintenance(id, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка при отправке на ТО"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
