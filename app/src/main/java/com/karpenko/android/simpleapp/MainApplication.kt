package com.karpenko.android.simpleapp

import android.app.Application
import com.karpenko.android.simpleapp.di.mainModule
import com.facebook.stetho.Stetho
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalSerializationApi
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidContext(this@MainApplication)
            modules(mainModule)
        }
    }
}