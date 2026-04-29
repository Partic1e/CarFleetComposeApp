package com.example.data.model.user

import com.google.gson.annotations.SerializedName

data class UserInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phoneNumber") val phoneNumber: String?
)