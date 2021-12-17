package com.karpenko.android.network

import com.karpenko.android.model.Forecast
import retrofit2.http.GET

interface ForecastApiService {
    @GET("https://5c5c8ba58d018a0014aa1b24.mockapi.io/api/forecast")
    suspend fun getForecast(): List<Forecast>
}