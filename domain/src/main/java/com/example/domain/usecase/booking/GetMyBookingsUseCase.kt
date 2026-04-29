package com.example.domain.usecase.booking

import com.example.domain.model.booking.Booking
import com.example.domain.repository.BookingRepository

interface GetMyBookingsUseCase {
    suspend operator fun invoke(): Result<List<Booking>>
}

class GetMyBookingsUseCaseImpl(
    private val bookingRepository: BookingRepository
) : GetMyBookingsUseCase {
    override suspend operator fun invoke(): Result<List<Booking>> {
        return bookingRepository.getMyBookings()
    }
}
