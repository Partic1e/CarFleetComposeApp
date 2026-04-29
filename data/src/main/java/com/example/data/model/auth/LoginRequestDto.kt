package com.example.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("password") val password: String
)
