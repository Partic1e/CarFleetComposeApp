package com.example.domain.model.car

data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val vinNumber: String,
    val color: String,
    val licensePlate: String,
    val type: String,
    val seatCount: Int,
    val currentMileageKm: Int,
    val fuelTankCapacityLiters: Int,
    val fuelType: String,
    val fuelConsumptionPer100Km: Double,
    val avgFuelPrice: Double,
    val status: Int,
    val isActive: Boolean
)
