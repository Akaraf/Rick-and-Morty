package com.raaf.rickandmorty

import android.app.Application
import android.content.res.Configuration
import com.raaf.rickandmorty.di.components.AppComponent
import com.raaf.rickandmorty.di.components.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .build()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}