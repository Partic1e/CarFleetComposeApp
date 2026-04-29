package com.example.domain.usecase.tripslot

import com.example.domain.model.tripslot.TripSlot
import com.example.domain.repository.TripSlotRepository

interface GetMySlotsUseCase {
    suspend operator fun invoke(): Result<List<TripSlot>>
}

class GetMySlotsUseCaseImpl(
    private val tripSlotRepository: TripSlotRepository
) : GetMySlotsUseCase {
    override suspend operator fun invoke(): Result<List<TripSlot>> {
        return tripSlotRepository.getMySlots()
    }
}
