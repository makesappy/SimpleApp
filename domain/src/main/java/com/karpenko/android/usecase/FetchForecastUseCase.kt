package com.karpenko.android.usecase

import com.karpenko.android.repository.ForecastRepository

/**
 * Calls repo and fetches remote api source
 */
class FetchForecastUseCase(
    private val repository: ForecastRepository
) {
    suspend operator fun invoke() = repository.fetchForecast()
}