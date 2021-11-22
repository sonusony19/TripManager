package com.trip.manager.koin

import com.trip.manager.firebase.FirebaseHelper
import org.koin.dsl.module

val networkModule = module {
    single {
        FirebaseHelper()
    }
}