package com.example.data.network

import com.example.data.model.notification.UpdateTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApi {
    @POST("notifications/token")
    suspend fun updateToken(@Body request: UpdateTokenRequest): Response<Unit>
}
