package com.karpenko.android.repository

import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    /**
     * Observes locally stored data, reacts on all DB changes.
     * It will return [Result.Success] in case of existing local data
     * or [Result.Error] if no data is stored locally
     */
    suspend fun observeForecast(): Flow<Result<List<Forecast>>>

    /**
     * Fetches remote data and stores it locally in case of success,
     * Note that it does NOT return stored data, it returns only [Result] with
     * [Unit] value of possible api call or insertion result.
     */
    suspend fun fetchForecast(): Result<Unit>
}