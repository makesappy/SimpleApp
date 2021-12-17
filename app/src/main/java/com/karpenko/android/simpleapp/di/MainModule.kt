package com.karpenko.android.simpleapp.di

import com.karpenko.android.simpleapp.viewmodel.ForecastViewModel
import com.karpenko.android.ForecastRepositoryImpl
import com.karpenko.android.repository.ForecastRepository
import com.karpenko.android.source.ForecastLocalSource
import com.karpenko.android.source.ForecastRemoteSource
import com.karpenko.android.usecase.FetchForecastUseCase
import com.karpenko.android.usecase.ObserveForecastUseCase
import com.karpenko.android.db.ForecastDatabase
import com.karpenko.android.db.buildDatabase
import com.karpenko.android.network.ForecastApiService
import com.karpenko.android.source.ForecastLocalSourceImpl
import com.karpenko.android.source.ForecastRemoteSourceImpl
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
val mainModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(StethoInterceptor())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }

    single { get<Retrofit>().create(ForecastApiService::class.java) }

    single { buildDatabase(context = get()) }

    single<ForecastRemoteSource> { ForecastRemoteSourceImpl(apiService = get()) }
    single<ForecastLocalSource> { ForecastLocalSourceImpl(dao = get<ForecastDatabase>().forecastDao()) }

    single<ForecastRepository> { ForecastRepositoryImpl(apiSource = get(), localSource = get()) }

    factory { ObserveForecastUseCase(repository = get()) }
    factory { FetchForecastUseCase(repository = get()) }

    viewModel { ForecastViewModel(observeForecastUseCase = get(), fetchForecastUseCase = get()) }
}