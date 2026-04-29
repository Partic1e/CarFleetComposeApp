package com.example.data.network

import com.example.data.model.tripslot.TripSlotResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface TripSlotApi {
    @GET("slots/my")
    suspend fun getMySlots(): Response<List<TripSlotResponseDto>>
}
