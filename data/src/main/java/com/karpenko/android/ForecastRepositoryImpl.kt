package com.karpenko.android

import com.karpenko.android.model.ErrorResult
import com.karpenko.android.model.Result
import com.karpenko.android.repository.ForecastRepository
import com.karpenko.android.source.ForecastLocalSource
import com.karpenko.android.source.ForecastRemoteSource
import kotlinx.coroutines.flow.map

class ForecastRepositoryImpl(
    private val apiSource: ForecastRemoteSource,
    private val localSource: ForecastLocalSource
) : ForecastRepository {
    override suspend fun observeForecast() =
        localSource.observeForecast().map {
            if (it != null) {
                Result.Success(it)
            } else {
                Result.Error(ErrorResult("No data is stored locally"))
            }
        }

    override suspend fun fetchForecast() =
        when (val remoteResult = apiSource.fetchForecast()) {
            is Result.Success -> localSource.insertForecast(remoteResult.data)
            is Result.Error -> Result.Error(remoteResult.error)
        }
}