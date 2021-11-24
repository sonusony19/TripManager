package com.trip.manager.baseclasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.helpers.PrefHelper
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel : ViewModel(), KoinComponent {
    val firebaseHelper: FirebaseHelper by inject()
    val prefHelper: PrefHelper by inject()

    fun checkLoginStatus() = MutableLiveData(firebaseHelper.auth.currentUser != null)
    fun logout() = firebaseHelper.auth.signOut()
}