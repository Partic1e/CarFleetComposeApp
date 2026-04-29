package com.example.data.network

import com.example.data.model.user.UpdateUserRequestDto
import com.example.data.model.user.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @GET("users/me")
    suspend fun getProfile(): Response<UserResponseDto>

    @PUT("users/me")
    suspend fun updateProfile(@Body request: UpdateUserRequestDto): Response<Unit>
}
