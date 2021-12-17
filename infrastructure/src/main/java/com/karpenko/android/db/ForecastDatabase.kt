package com.karpenko.android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ForecastEntity::class],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}

private const val DATABASE_FILE_NAME = "forecast.db"
fun buildDatabase(context: Context) = Room.databaseBuilder(
    context.applicationContext,
    ForecastDatabase::class.java,
    DATABASE_FILE_NAME
).fallbackToDestructiveMigration().build()