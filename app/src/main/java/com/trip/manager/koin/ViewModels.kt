package com.trip.manager.koin

import com.trip.manager.ui.home.viewmodel.MainViewModel
import com.trip.manager.ui.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}