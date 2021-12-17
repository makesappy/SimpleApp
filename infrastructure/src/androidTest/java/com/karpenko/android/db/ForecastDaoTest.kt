package com.karpenko.android.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.karpenko.android.model.Forecast
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ForecastDaoTest {
    private val domain = Forecast(
        day = 1,
        description = "desc",
        sunrise = 1L,
        sunset = 1L,
        chanceRain = 1.0,
        high = 1,
        low = 1,
        imageSrc = "src",
    )

    private lateinit var dao: ForecastDao
    private lateinit var db: ForecastDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, ForecastDatabase::class.java
        ).build()
        dao = db.forecastDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadTest() {
        val entity = domain.toEntity()

        runBlocking {
            dao.insert(entity)
        }

        val loaded = runBlocking {
            dao.observe().first()?.first()
        }

        loaded?.day shouldBe entity.day
        loaded?.description shouldBe entity.description
        loaded?.sunrise shouldBe entity.sunrise
        loaded?.sunset shouldBe entity.sunset
        loaded?.chanceRain shouldBe entity.chanceRain
        loaded?.high shouldBe entity.high
        loaded?.low shouldBe entity.low
        loaded?.imageSrc shouldBe entity.imageSrc
    }
}