package com.example.data.model.booking

import com.google.gson.annotations.SerializedName

data class CancelBookingResponseDto(
    @SerializedName("success") val success: Boolean
)
