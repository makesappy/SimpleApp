package com.karpenko.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    val day: Int,
    val description: String,
    val sunrise: Long,
    val sunset: Long,
    @SerialName("chance_rain")
    val chanceRain: Double,
    val high: Int,
    val low: Int,
    @SerialName("image")
    val imageSrc: String
)