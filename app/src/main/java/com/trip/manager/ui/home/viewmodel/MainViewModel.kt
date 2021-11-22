package com.trip.manager.ui.home.viewmodel

import android.util.Log
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.helpers.PrefHelper
import com.trip.manager.utils.TAG

class MainViewModel(private val firebaseHelper: FirebaseHelper, private val prefHelper: PrefHelper) : BaseViewModel() {

    fun showLog() {
        Log.d(TAG, "showLog: ${if (firebaseHelper == null) "Null" else "not null"}")
        firebaseHelper.showLog()
    }
}