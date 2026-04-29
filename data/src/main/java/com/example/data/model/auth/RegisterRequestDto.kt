package com.example.data.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("password") val password: String,
    @SerializedName("userRole") val userRole: Int,
    @SerializedName("post") val post: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("patronymic") val patronymic: String?,
    @SerializedName("email") val email: String?
)
