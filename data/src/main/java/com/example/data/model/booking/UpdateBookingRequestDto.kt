package com.example.data.model.booking

import com.google.gson.annotations.SerializedName

data class UpdateBookingRequestDto(
    @SerializedName("FromAddress") val fromAddress: String?,
    @SerializedName("ToAddress") val toAddress: String?,
    @SerializedName("DesiredDeparture") val desiredDeparture: String?,
    @SerializedName("Seats") val seats: Int?,
    @SerializedName("Purpose") val purpose: String?
)
