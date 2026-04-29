package com.example.domain.model.booking

data class TripRequestResult(
    val bookingId: String,
    val tripSlotId: String,
    val isNewSlot: Boolean,
    val slotStatus: String
)
