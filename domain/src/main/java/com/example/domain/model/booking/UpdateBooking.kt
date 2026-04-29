package com.example.domain.model.booking

data class UpdateBooking(
    val fromAddress: String? = null,
    val toAddress: String? = null,
    val desiredDeparture: String? = null,
    val seats: Int? = null,
    val purpose: String? = null
)
