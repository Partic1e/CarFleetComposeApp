package com.example.domain.repository

import com.example.domain.model.tripslot.TripSlot

interface TripSlotRepository {
    suspend fun getMySlots(): Result<List<TripSlot>>
}
