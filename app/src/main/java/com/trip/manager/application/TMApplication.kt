package com.trip.manager.application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.trip.manager.BuildConfig
import com.trip.manager.koin.appModule
import com.trip.manager.koin.networkModule
import com.trip.manager.koin.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TMApplication : MultiDexApplication() {

    companion object {
        lateinit var appContext: Context
        fun getSharedPreferences() = appContext.getSharedPreferences("${BuildConfig.APPLICATION_ID}_app", Context.MODE_PRIVATE)!!
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidContext(this@TMApplication)
            modules(appModule, networkModule, viewModels)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}