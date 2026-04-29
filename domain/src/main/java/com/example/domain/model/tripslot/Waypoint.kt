package com.example.domain.model.tripslot

data class Waypoint(
    val passengerId: String,
    val address: String,
    val type: String,
    val estimatedArrivalTime: String,
    val lat: Double?,
    val lon: Double?
)
