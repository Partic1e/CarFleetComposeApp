package com.example.data.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("expiresInSeconds") val expiresInSeconds: Int,
    @SerializedName("userRole") val userRole: Int
)