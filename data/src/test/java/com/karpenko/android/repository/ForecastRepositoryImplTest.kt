package com.karpenko.android.repository

import com.karpenko.android.ForecastRepositoryImpl
import com.karpenko.android.model.ErrorResult
import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import com.karpenko.android.source.ForecastLocalSource
import com.karpenko.android.source.ForecastRemoteSource
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ForecastRepositoryImplTest {

    @Test
    fun `observeForecast should return ResultSuccess if emitted value from local source is not null`() {
        val localSource = mockk<ForecastLocalSource> {
            coEvery { observeForecast() } returns flowOf(emptyList())
        }
        val repo = ForecastRepositoryImpl(mockk(), localSource)
        val result = runBlocking {
            repo.observeForecast().first()
        }
        coVerify(exactly = 1) {
            localSource.observeForecast()
        }
        result.shouldBeInstanceOf<Result.Success<List<Forecast>>>()
    }

    @Test
    fun `observeForecast should return ResultError if emitted value from local source is null`() {
        val localSource = mockk<ForecastLocalSource> {
            coEvery { observeForecast() } returns flowOf(null)
        }
        val repo = ForecastRepositoryImpl(mockk(), localSource)
        val result = runBlocking {
            repo.observeForecast().first()
        }
        coVerify(exactly = 1) {
            localSource.observeForecast()
        }
        result.shouldBeInstanceOf<Result.Error<List<Forecast>>>()
    }

    @Test
    fun `if fetchForecast from api source returns ResultSuccess - insert is called and returns ResultSuccess from local source`() {
        val remoteSource = mockk<ForecastRemoteSource> {
            coEvery { fetchForecast() } returns Result.Success(emptyList())
        }
        val localSource = mockk<ForecastLocalSource> {
            coEvery { insertForecast(any()) } returns Result.Success(Unit)
        }
        val repo = ForecastRepositoryImpl(remoteSource, localSource)
        val result = runBlocking {
            repo.fetchForecast()
        }
        coVerify(exactly = 1) {
            remoteSource.fetchForecast()
            localSource.insertForecast(any())
        }
        result.shouldBeInstanceOf<Result.Success<Unit>>()
    }

    @Test
    fun `if fetchForecast from api source returns ResultSuccess - insert is called and returns ResultError from local source`() {
        val remoteSource = mockk<ForecastRemoteSource> {
            coEvery { fetchForecast() } returns Result.Success(emptyList())
        }
        val localSource = mockk<ForecastLocalSource> {
            coEvery { insertForecast(any()) } returns Result.Error(ErrorResult(""))
        }
        val repo = ForecastRepositoryImpl(remoteSource, localSource)
        val result = runBlocking {
            repo.fetchForecast()
        }
        coVerify(exactly = 1) {
            remoteSource.fetchForecast()
            localSource.insertForecast(any())
        }
        result.shouldBeInstanceOf<Result.Error<Unit>>()
    }

    @Test
    fun `if fetchForecast from api source returns ResultError - local source is not called, and ResultError is returned`() {
        val remoteSource = mockk<ForecastRemoteSource> {
            coEvery { fetchForecast() } returns Result.Error(ErrorResult(""))
        }
        val localSource = mockk<ForecastLocalSource>()
        val repo = ForecastRepositoryImpl(remoteSource, localSource)
        val result = runBlocking {
            repo.fetchForecast()
        }
        coVerify(exactly = 1) {
            remoteSource.fetchForecast()
        }
        coVerify(inverse = true) {
            localSource.insertForecast(any())
        }
        result.shouldBeInstanceOf<Result.Error<Unit>>()
    }
}