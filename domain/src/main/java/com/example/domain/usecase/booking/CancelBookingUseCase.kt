package com.example.domain.usecase.booking

import com.example.domain.repository.BookingRepository

interface CancelBookingUseCase {
    suspend operator fun invoke(bookingId: String): Result<Boolean>
}

class CancelBookingUseCaseImpl(
    private val bookingRepository: BookingRepository
) : CancelBookingUseCase {
    override suspend operator fun invoke(bookingId: String): Result<Boolean> {
        return bookingRepository.cancelBooking(bookingId)
    }
}
