package com.karpenko.android.source

import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import com.karpenko.android.db.ForecastDao
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ForecastLocalSourceImplTest {

    @Test
    fun `observeForecast should return null result from dao`() {
        val dao = mockk<ForecastDao> {
            coEvery { observe() } returns flowOf(null)
        }
        val source = ForecastLocalSourceImpl(dao)
        val result = runBlocking {
            source.observeForecast().first()
        }
        coVerify(exactly = 1) {
            dao.observe()
        }
        result shouldBe null
    }

    @Test
    fun `observeForecast should return list with forecast from dao`() {
        val list = mockk<List<Forecast>>()
        val dao = mockk<ForecastDao> {
            coEvery { observe() } returns flowOf(list)
        }
        val source = ForecastLocalSourceImpl(dao)
        val result = runBlocking {
            source.observeForecast().first()
        }
        coVerify(exactly = 1) {
            dao.observe()
        }
        result shouldBe list
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun `insertForecast should return ResultSuccess if all insertions to dao succeeded`() {
        val list =
            listOf<Forecast>(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val dao = mockk<ForecastDao> {
            var lastInsertionValue = 1L
            coEvery { insert(any()) } returns lastInsertionValue++
        }
        val source = ForecastLocalSourceImpl(dao)
        val result = runBlocking {
            source.insertForecast(list)
        }
        coVerify(exactly = list.size) {
            dao.insert(any())
        }
        result.shouldBeInstanceOf<Result.Success<Unit>>()
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun `insertForecast should return ResultError if at least 1 insertion to dao failed`() {
        val list =
            listOf<Forecast>(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val dao = mockk<ForecastDao> {
            coEvery { insert(any()) } returns -1L
        }
        val source = ForecastLocalSourceImpl(dao)
        val result = runBlocking {
            source.insertForecast(list)
        }
        coVerify(exactly = list.size) {
            dao.insert(any())
        }
        result.shouldBeInstanceOf<Result.Error<Unit>>()
    }
}