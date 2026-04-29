package com.example.data.model.booking

import com.example.data.model.user.UserInfoDto
import com.google.gson.annotations.SerializedName

data class BookingResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("tripSlotId") val tripSlotId: String,
    @SerializedName("seats") val seats: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("fromAddress") val fromAddress: String,
    @SerializedName("toAddress") val toAddress: String,
    @SerializedName("departureTime") val departureTime: String,
    @SerializedName("carDisplayName") val carDisplayName: String?,
    @SerializedName("driver") val driver: UserInfoDto?,
    @SerializedName("otherPassengers") val otherPassengers: List<UserInfoDto>?
)
