package com.karpenko.android.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karpenko.android.model.Forecast

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey
    val day: Int,
    val description: String,
    val sunrise: Long,
    val sunset: Long,
    val chanceRain: Double,
    val high: Int,
    val low: Int,
    val imageSrc: String
)

fun Forecast.toEntity() =
    ForecastEntity(
        day, description, sunrise, sunset, chanceRain, high, low, imageSrc
    )