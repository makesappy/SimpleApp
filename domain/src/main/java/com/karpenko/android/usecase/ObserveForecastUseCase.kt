package com.karpenko.android.usecase

import com.karpenko.android.repository.ForecastRepository

/**
 * Observes all changes in local DB
 */
class ObserveForecastUseCase(
    private val repository: ForecastRepository
) {
    suspend operator fun invoke() = repository.observeForecast()
}