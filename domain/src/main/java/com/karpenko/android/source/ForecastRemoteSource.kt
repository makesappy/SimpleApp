package com.karpenko.android.source

import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result

interface ForecastRemoteSource {
    /**
     * Fetches api service, and returns [Result.Success] in case of  successful response,
     * or [Result.Error] in case of any failure, including no internet connection
     */
    suspend fun fetchForecast(): Result<List<Forecast>>
}