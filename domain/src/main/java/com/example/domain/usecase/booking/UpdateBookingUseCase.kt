package com.example.domain.usecase.booking

import com.example.domain.model.booking.UpdateBooking
import com.example.domain.repository.BookingRepository

interface UpdateBookingUseCase {
    suspend operator fun invoke(bookingId: String, params: UpdateBooking): Result<Unit>
}

class UpdateBookingUseCaseImpl(
    private val bookingRepository: BookingRepository
) : UpdateBookingUseCase {
    override suspend operator fun invoke(bookingId: String, params: UpdateBooking): Result<Unit> {
        if (params.seats != null && params.seats <= 0) {
            return Result.failure(IllegalArgumentException("Количество мест должно быть больше 0"))
        }
        if (params.fromAddress?.isBlank() == true) {
            return Result.failure(IllegalArgumentException("Адрес отправления не может быть пустым"))
        }

        return bookingRepository.updateBooking(bookingId, params)
    }
}
