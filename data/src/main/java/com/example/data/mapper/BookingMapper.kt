package com.example.data.mapper

import com.example.data.model.booking.BookingResponseDto
import com.example.data.model.booking.TripRequestDto
import com.example.data.model.booking.TripRequestResponseDto
import com.example.domain.model.booking.Booking
import com.example.domain.model.booking.TripRequest
import com.example.domain.model.booking.TripRequestResult

fun TripRequest.toDto(): TripRequestDto {
    return TripRequestDto(
        fromAddress = this.fromAddress,
        toAddress = this.toAddress,
        desiredDeparture = this.desiredDeparture,
        seats = this.seats,
        purpose = this.purpose
    )
}

fun TripRequestResponseDto.toDomain(): TripRequestResult {
    return TripRequestResult(
        bookingId = this.bookingId,
        tripSlotId = this.tripSlotId,
        isNewSlot = this.isNewSlot,
        slotStatus = this.slotStatus
    )
}

fun BookingResponseDto.toDomain(): Booking {
    return Booking(
        id = this.id,
        fromAddress = this.fromAddress,
        toAddress = this.toAddress,
        departureTime = this.departureTime,
        status = this.status,
        carDisplayName = this.carDisplayName ?: "Машина не назначена",
        driver = this.driver?.toDomain(),
        otherPassengers = this.otherPassengers?.map { it.toDomain() } ?: emptyList()
    )
}
