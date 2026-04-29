package com.example.data.network

import com.example.data.model.booking.BookingResponseDto
import com.example.data.model.booking.CancelBookingResponseDto
import com.example.data.model.booking.TripRequestDto
import com.example.data.model.booking.TripRequestResponseDto
import com.example.data.model.booking.UpdateBookingRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApi {
    @POST("bookings/request")
    suspend fun requestTrip(@Body request: TripRequestDto): Response<TripRequestResponseDto>

    @GET("bookings/my")
    suspend fun getMyBookings(): Response<List<BookingResponseDto>>

    @PATCH("bookings/{bookingId}")
    suspend fun updateBooking(@Path("bookingId") bookingId: String, @Body request: UpdateBookingRequestDto): Response<Unit>

    @POST("bookings/{bookingId}/cancel")
    suspend fun cancelBooking(@Path("bookingId") bookingId: String): Response<CancelBookingResponseDto>
}
