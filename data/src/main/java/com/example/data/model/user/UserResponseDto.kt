package com.example.data.model.user

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("role") val role: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("patronymic") val patronymic: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("post") val post: String,
    @SerializedName("isActive") val isActive: Boolean
)
