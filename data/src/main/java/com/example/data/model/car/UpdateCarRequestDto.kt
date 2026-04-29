package com.example.data.model.car

import com.google.gson.annotations.SerializedName

data class UpdateCarRequestDto(
    @SerializedName("brand") val brand: String,
    @SerializedName("model") val model: String,
    @SerializedName("year") val year: Int,
    @SerializedName("vinNumber") val vinNumber: String,
    @SerializedName("color") val color: String,
    @SerializedName("licensePlate") val licensePlate: String,
    @SerializedName("type") val type: String,
    @SerializedName("seatCount") val seatCount: Int,
    @SerializedName("currentMileageKm") val currentMileageKm: Int,
    @SerializedName("fuelTankCapacityLiters") val fuelTankCapacityLiters: Int,
    @SerializedName("fuelType") val fuelType: String,
    @SerializedName("fuelConsumptionPer100Km") val fuelConsumptionPer100Km: Double,
    @SerializedName("avgFuelPrice") val avgFuelPrice: Double,
    @SerializedName("status") val status: Int,
    @SerializedName("isActive") val isActive: Boolean
)
