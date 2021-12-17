package com.karpenko.android.usecase

import com.karpenko.android.model.ErrorResult
import com.karpenko.android.model.Result
import com.karpenko.android.repository.ForecastRepository
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FetchForecastUseCaseTest {

    @Test
    fun `FetchForecastUseCase should call repository and return ResultSuccess from repository`() {
        val repo = mockk<ForecastRepository> {
            coEvery { fetchForecast() } returns Result.Success(Unit)
        }
        val uc = FetchForecastUseCase(repo)
        val result = runBlocking {
            uc()
        }
        coVerify(exactly = 1) {
            repo.fetchForecast()
        }
        result.shouldBeInstanceOf<Result.Success<Unit>>()
    }

    @Test
    fun `FetchForecastUseCase should call repository and return ResultError from repository`() {
        val repo = mockk<ForecastRepository> {
            coEvery { fetchForecast() } returns Result.Error(ErrorResult(""))
        }
        val uc = FetchForecastUseCase(repo)
        val result = runBlocking {
            uc()
        }
        coVerify(exactly = 1) {
            repo.fetchForecast()
        }
        result.shouldBeInstanceOf<Result.Error<Unit>>()
    }
}