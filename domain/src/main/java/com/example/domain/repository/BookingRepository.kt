package com.example.domain.repository

import com.example.domain.model.booking.Booking
import com.example.domain.model.booking.TripRequest
import com.example.domain.model.booking.TripRequestResult
import com.example.domain.model.booking.UpdateBooking

interface BookingRepository {
    suspend fun requestTrip(request: TripRequest): Result<TripRequestResult>
    suspend fun getMyBookings(): Result<List<Booking>>
    suspend fun updateBooking(bookingId: String, params: UpdateBooking): Result<Unit>
    suspend fun cancelBooking(bookingId: String): Result<Boolean>
}
