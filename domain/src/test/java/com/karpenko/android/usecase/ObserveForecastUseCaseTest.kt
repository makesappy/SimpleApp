package com.karpenko.android.usecase

import com.karpenko.android.model.ErrorResult
import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import com.karpenko.android.repository.ForecastRepository
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ObserveForecastUseCaseTest {

    @Test
    fun `ObserveForecastUseCase should call repository and return ResultSuccess from repository`() {
        val repo = mockk<ForecastRepository> {
            coEvery { observeForecast() } returns flowOf(Result.Success(listOf(mockk())))
        }
        val uc = ObserveForecastUseCase(repo)
        val result = runBlocking {
            uc().first()
        }
        coVerify(exactly = 1) {
            repo.observeForecast()
        }
        result.shouldBeInstanceOf<Result.Success<List<Forecast>>>()
    }

    @Test
    fun `ObserveForecastUseCase should call repository and return ResultError from repository`() {
        val repo = mockk<ForecastRepository> {
            coEvery { observeForecast() } returns flowOf(Result.Error(ErrorResult("")))
        }
        val uc = ObserveForecastUseCase(repo)
        val result = runBlocking {
            uc().first()
        }
        coVerify(exactly = 1) {
            repo.observeForecast()
        }
        result.shouldBeInstanceOf<Result.Error<List<Forecast>>>()
    }
}