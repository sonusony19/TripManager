package com.trip.manager.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.listeners.FirebaseAuthListener
import com.trip.manager.ui.login.model.LoginRequest
import com.trip.manager.utils.getStringResource

class LoginViewModel : BaseViewModel() {

    fun loginUser(loginRequest: LoginRequest, listener: FirebaseAuthListener) = firebaseHelper.signInUser(loginRequest, listener)

    fun validateCredentials(loginRequest: LoginRequest): LiveData<String> {
        val error = when {
            loginRequest.email.isEmpty() -> getStringResource(R.string.please_enter_your_email)
            loginRequest.password.isEmpty() -> getStringResource(R.string.please_enter_your_password)
            else -> ""
        }
        return MutableLiveData(error)
    }
}