package com.example.data.model.booking

import com.google.gson.annotations.SerializedName

data class TripRequestDto(
    @SerializedName("fromAddress") val fromAddress: String,
    @SerializedName("toAddress") val toAddress: String,
    @SerializedName("desiredDeparture") val desiredDeparture: String,
    @SerializedName("seats") val seats: Int,
    @SerializedName("purpose") val purpose: String
)
