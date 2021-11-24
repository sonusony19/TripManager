package com.trip.manager.koin

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
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
    factory {
        val layoutManager = GridLayoutManager(androidContext(), 2)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int) = 1
        }
        layoutManager
    }
}