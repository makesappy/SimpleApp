package com.karpenko.android.source

import com.karpenko.android.model.Result
import com.karpenko.android.model.safeCall
import com.karpenko.android.network.ForecastApiService

class ForecastRemoteSourceImpl(
    private val apiService: ForecastApiService
) : ForecastRemoteSource {

    override suspend fun fetchForecast() =
        safeCall(
            { Result.Success(apiService.getForecast()) },
            "Fetching forecast failed"
        )
}