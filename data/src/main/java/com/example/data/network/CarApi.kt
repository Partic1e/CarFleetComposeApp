package com.example.data.network

import com.example.data.model.car.CarResponseDto
import com.example.data.model.car.SetMaintenanceRequestDto
import com.example.data.model.car.UpdateCarRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CarApi {
    @GET("cars/{id}")
    suspend fun getCarById(@Path("id") id: String): Response<CarResponseDto>

    @PUT("cars/{id}")
    suspend fun updateCar(@Path("id") id: String, @Body request: UpdateCarRequestDto): Response<Unit>

    @POST("cars/{id}/maintenance")
    suspend fun setMaintenance(@Path("id") id: String, @Body request: SetMaintenanceRequestDto): Response<Unit>
}
