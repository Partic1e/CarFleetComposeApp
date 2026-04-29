package com.example.data.network

import com.example.data.model.location.LocationUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackingApi {
    @POST("location")
    suspend fun updateLocation(@Body request: LocationUpdateRequest): Response<Unit>
}
