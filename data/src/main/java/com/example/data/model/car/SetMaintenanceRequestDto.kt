package com.example.data.model.car

import com.google.gson.annotations.SerializedName

data class SetMaintenanceRequestDto(
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?,
    @SerializedName("comment") val comment: String?
)
