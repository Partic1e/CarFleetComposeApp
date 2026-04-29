package com.example.data.model.location

import com.google.gson.annotations.SerializedName

data class LocationUpdateRequest(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
