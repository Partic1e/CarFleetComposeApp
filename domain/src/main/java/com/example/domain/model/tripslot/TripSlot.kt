package com.example.domain.model.tripslot

import com.example.domain.model.user.UserInfo

data class TripSlot(
    val id: String,
    val carId: String?,
    val driverId: String?,
    val fromAddress: String,
    val toAddress: String,
    val departureTime: String,
    val arrivalPlanned: String?,
    val seatCapacity: Int,
    val seatsTaken: Int,
    val status: Int,
    val carDisplayName: String,
    val driverFullName: String?,
    val passengers: List<UserInfo>,
    val fromLat: Double?,
    val fromLon: Double?,
    val toLat: Double?,
    val toLon: Double?,
    val waypoints: List<Waypoint>
)
