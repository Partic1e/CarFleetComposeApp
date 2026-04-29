package com.example.domain.model.booking

data class TripRequest(
    val fromAddress: String,
    val toAddress: String,
    val desiredDeparture: String,
    val seats: Int,
    val purpose: String
)
