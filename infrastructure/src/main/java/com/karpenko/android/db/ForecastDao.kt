package com.karpenko.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karpenko.android.model.Forecast
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ForecastEntity): Long

    @Query("SELECT * from forecast")
    fun observe(): Flow<List<Forecast>?>
}