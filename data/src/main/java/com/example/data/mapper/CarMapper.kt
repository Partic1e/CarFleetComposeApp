package com.example.data.mapper

import com.example.data.model.car.CarResponseDto
import com.example.data.model.car.UpdateCarRequestDto
import com.example.domain.model.car.Car

fun CarResponseDto.toDomain(): Car {
    return Car(
        id = this.id,
        brand = this.brand,
        model = this.model,
        year = this.year,
        vinNumber = this.vinNumber,
        color = this.color,
        licensePlate = this.licensePlate,
        type = this.type,
        seatCount = this.seatCount,
        currentMileageKm = this.currentMileageKm,
        fuelTankCapacityLiters = this.fuelTankCapacityLiters,
        fuelType = this.fuelType,
        fuelConsumptionPer100Km = this.fuelConsumptionPer100Km,
        avgFuelPrice = this.avgFuelPrice,
        status = this.status,
        isActive = this.isActive
    )
}

fun Car.toRequestDto(newMileage: Int? = null): UpdateCarRequestDto {
    return UpdateCarRequestDto(
        brand = this.brand,
        model = this.model,
        year = this.year,
        vinNumber = this.vinNumber,
        color = this.color,
        licensePlate = this.licensePlate,
        type = this.type,
        seatCount = this.seatCount,
        currentMileageKm = newMileage ?: this.currentMileageKm,
        fuelTankCapacityLiters = this.fuelTankCapacityLiters,
        fuelType = this.fuelType,
        fuelConsumptionPer100Km = this.fuelConsumptionPer100Km,
        avgFuelPrice = this.avgFuelPrice,
        status = this.status,
        isActive = this.isActive
    )
}
