package com.trip.manager.ui.home.viewmodel

import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.helpers.PrefHelper

class MainViewModel(private val firebaseHelper: FirebaseHelper, private val prefHelper: PrefHelper) : BaseViewModel()