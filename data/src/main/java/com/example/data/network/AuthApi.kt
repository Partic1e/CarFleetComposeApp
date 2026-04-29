package com.example.data.network

import com.example.data.model.auth.AuthResponseDto
import com.example.data.model.auth.LoginRequestDto
import com.example.data.model.auth.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<AuthResponseDto>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
