package com.karpenko.android.source

import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import com.karpenko.android.network.ForecastApiService
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ForecastRemoteSourceImplTest {

    @Test
    fun `fetchForecast should return ResultSuccess from api`() {
        val api = mockk<ForecastApiService> {
            coEvery { getForecast() } returns listOf()
        }
        val source = ForecastRemoteSourceImpl(api)
        val result = runBlocking {
            source.fetchForecast()
        }
        coVerify(exactly = 1) {
            api.getForecast()
        }
        result.shouldBeInstanceOf<Result.Success<List<Forecast>>>()
    }

    @Test
    fun `fetchForecast should return ResultError from api`() {
        val api = mockk<ForecastApiService> {
            coEvery { getForecast() } throws Exception("mocked exception")
        }
        val source = ForecastRemoteSourceImpl(api)
        val result = runBlocking {
            source.fetchForecast()
        }
        coVerify(exactly = 1) {
            api.getForecast()
        }
        result.shouldBeInstanceOf<Result.Error<List<Forecast>>>()
    }
}