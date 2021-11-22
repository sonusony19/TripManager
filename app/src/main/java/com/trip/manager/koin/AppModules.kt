package com.trip.manager.koin

import android.content.Context
import com.trip.manager.BuildConfig
import com.trip.manager.helpers.PrefHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        androidContext().getSharedPreferences("${BuildConfig.APPLICATION_ID}_app", Context.MODE_PRIVATE)
    }
    single {
        PrefHelper(get())
    }
}