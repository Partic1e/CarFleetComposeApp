package com.example.domain.usecase.booking

import com.example.domain.model.booking.TripRequest
import com.example.domain.model.booking.TripRequestResult
import com.example.domain.repository.BookingRepository

interface RequestTripUseCase {
    suspend operator fun invoke(request: TripRequest): Result<TripRequestResult>
}

class RequestTripUseCaseImpl(
    private val bookingRepository: BookingRepository
) : RequestTripUseCase {
    override suspend operator fun invoke(request: TripRequest): Result<TripRequestResult> {
        return bookingRepository.requestTrip(request)
    }
}
