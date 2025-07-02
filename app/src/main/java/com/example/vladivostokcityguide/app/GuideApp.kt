package com.example.vladivostokcityguide.app

import android.app.Application
import com.example.vladivostokcityguide.di.GuideAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GuideApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GuideApp)
            modules(GuideAppModule)
        }
    }
}