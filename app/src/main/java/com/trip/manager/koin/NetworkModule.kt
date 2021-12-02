package com.trip.manager.koin

import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.network.MainRepository
import com.trip.manager.network.TripRepository
import com.trip.manager.network.UserRepository
import org.koin.dsl.module

val networkModule = module {
    single {
        FirebaseHelper()
    }
    factory {
        MainRepository(get())
    }
    factory {
        TripRepository(get())
    }
    factory {
        UserRepository(get())
    }
}