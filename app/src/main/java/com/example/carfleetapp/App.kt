package com.example.carfleetapp

import android.app.Application
import com.example.carfleetapp.presentation.di.appModule
import com.google.firebase.FirebaseApp
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_API_KEY)
        MapKitFactory.setLocale("ru_RU")
        MapKitFactory.initialize(this)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}
