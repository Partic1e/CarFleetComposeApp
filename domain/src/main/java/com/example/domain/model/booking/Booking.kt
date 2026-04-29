package com.example.domain.model.booking

import com.example.domain.model.user.UserInfo

data class Booking(
    val id: String,
    val fromAddress: String,
    val toAddress: String,
    val departureTime: String,
    val status: Int,
    val carDisplayName: String,
    val driver: UserInfo?,
    val otherPassengers: List<UserInfo>
)
