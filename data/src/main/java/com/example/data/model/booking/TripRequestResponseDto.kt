package com.example.data.model.booking

import com.google.gson.annotations.SerializedName

data class TripRequestResponseDto(
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("tripSlotId") val tripSlotId: String,
    @SerializedName("isNewSlot") val isNewSlot: Boolean,
    @SerializedName("slotStatus") val slotStatus: String
)
