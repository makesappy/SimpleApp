package com.karpenko.android.source

import com.karpenko.android.model.ErrorResult
import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import com.karpenko.android.db.ForecastDao
import com.karpenko.android.db.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ForecastLocalSourceImpl(
    private val dao: ForecastDao
) : ForecastLocalSource {
    override suspend fun observeForecast(): Flow<List<Forecast>?> =
        dao.observe()

    override suspend fun insertForecast(data: List<Forecast>): Result<Unit> {
        val successfulInsertions =
            withContext(Dispatchers.IO) { data.map { dao.insert(it.toEntity()) != -1L } }
        return if (successfulInsertions.contains(false)) {
            Result.Error(ErrorResult("Insertion failed"))
        } else {
            Result.Success(Unit)
        }
    }
}