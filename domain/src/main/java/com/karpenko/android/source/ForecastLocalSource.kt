package com.karpenko.android.source

import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import kotlinx.coroutines.flow.Flow

interface ForecastLocalSource {
    /**
     * Observes locally stored data, reacts on all DB changes.
     * It will return [Flow] with non-null value in case of existing local data
     * or [Flow] with null value if no data is stored locally
     */
    suspend fun observeForecast(): Flow<List<Forecast>?>

    /**
     * Inserts data in local DB
     * returns insertion result
     * If at least one insertion failed - [Result.Error] is returned
     * otherwise - [Result.Success] is returned
     */
    suspend fun insertForecast(data: List<Forecast>): Result<Unit>
}