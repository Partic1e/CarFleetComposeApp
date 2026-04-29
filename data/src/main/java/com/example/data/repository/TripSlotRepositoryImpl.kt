package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.network.TripSlotApi
import com.example.domain.model.tripslot.TripSlot
import com.example.domain.repository.TripSlotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TripSlotRepositoryImpl(
    private val api: TripSlotApi
) : TripSlotRepository {
    override suspend fun getMySlots(): Result<List<TripSlot>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMySlots()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.map { it.toDomain() })
                } else {
                    Result.failure(Exception("Не удалось загрузить рейсы"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
