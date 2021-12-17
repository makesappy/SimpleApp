package com.karpenko.android.db

import com.karpenko.android.model.Forecast
import io.kotest.matchers.shouldBe
import org.junit.Test

class ForecastEntityTest {
    @Test
    fun `domain model fields should be same as entity fields after mapping`() {
        val domain = Forecast(
            day = 1,
            description = "desc",
            sunrise = 1L,
            sunset = 1L,
            chanceRain = 1.0,
            high = 1,
            low = 1,
            imageSrc = "src"
        )
        val entity = domain.toEntity()

        domain.day shouldBe entity.day
        domain.description shouldBe entity.description
        domain.sunrise shouldBe entity.sunrise
        domain.sunset shouldBe entity.sunset
        domain.chanceRain shouldBe entity.chanceRain
        domain.high shouldBe entity.high
        domain.low shouldBe entity.low
        domain.imageSrc shouldBe entity.imageSrc
    }
}