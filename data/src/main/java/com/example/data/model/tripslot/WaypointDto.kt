package com.example.data.model.tripslot

import com.google.gson.annotations.SerializedName

data class WaypointDto(
    @SerializedName("passengerId") val passengerId: String,
    @SerializedName("address") val address: String,
    @SerializedName("type") val type: String,
    @SerializedName("estimatedArrivalTime") val estimatedArrivalTime: String,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?
)
