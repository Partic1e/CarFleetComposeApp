package com.example.data.model.tripslot

import com.example.data.model.user.UserInfoDto
import com.google.gson.annotations.SerializedName

data class TripSlotResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("carId") val carId: String?,
    @SerializedName("driverId") val driverId: String?,
    @SerializedName("fromAddress") val fromAddress: String,
    @SerializedName("toAddress") val toAddress: String,
    @SerializedName("departureTime") val departureTime: String,
    @SerializedName("arrivalPlanned") val arrivalPlanned: String?,
    @SerializedName("seatCapacity") val seatCapacity: Int,
    @SerializedName("seatsTaken") val seatsTaken: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("carDisplayName") val carDisplayName: String?,
    @SerializedName("driverFullName") val driverFullName: String?,
    @SerializedName("passengers") val passengers: List<UserInfoDto>?,
    @SerializedName("fromLat") val fromLat: Double?,
    @SerializedName("fromLon") val fromLon: Double?,
    @SerializedName("toLat") val toLat: Double?,
    @SerializedName("toLon") val toLon: Double?,
    @SerializedName("waypoints") val waypoints: List<WaypointDto>?
)
