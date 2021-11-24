package com.trip.manager.koin

import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.ui.home.viewmodel.MainViewModel
import com.trip.manager.ui.login.viewmodel.LoginViewModel
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.ui.user.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { BaseViewModel() }
    viewModel { TripViewModel(get()) }
    viewModel { UserViewModel(get()) }
}