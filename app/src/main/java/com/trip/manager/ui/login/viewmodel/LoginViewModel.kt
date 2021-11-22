package com.trip.manager.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.helpers.PrefHelper
import com.trip.manager.listeners.FirebaseAuthListener
import com.trip.manager.utils.getStringResource

class LoginViewModel(private val firebaseHelper: FirebaseHelper, private val prefHelper: PrefHelper) : BaseViewModel() {

    fun loginUser(email: String, password: String, listener: FirebaseAuthListener) {
        firebaseHelper.signInUser(email, password, listener)
    }

    fun validateCredentials(email: String, password: String): LiveData<String> {
        val error = when {
            email.isEmpty() -> getStringResource(R.string.please_enter_your_email)
            password.isEmpty() -> getStringResource(R.string.please_enter_your_password)
            else -> ""
        }
        return MutableLiveData(error)
    }
}