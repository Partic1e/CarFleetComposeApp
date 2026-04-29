package com.example.data.model.user

import com.google.gson.annotations.SerializedName

data class UpdateUserRequestDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("patronymic") val patronymic: String,
    @SerializedName("email") val email: String,
    @SerializedName("post") val post: String
)
