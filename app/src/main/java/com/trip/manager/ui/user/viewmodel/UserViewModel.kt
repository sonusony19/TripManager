package com.trip.manager.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.baseclasses.Response
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.network.UserRepository
import com.trip.manager.ui.user.model.User
import com.trip.manager.utils.getStringResource

class UserViewModel(private val repository: UserRepository) : BaseViewModel() {

    fun validateAndCreateUser(user: User): MutableLiveData<Response<Any>> {
        val error = when {
            user.name.isEmpty() -> getStringResource(R.string.please_enter_the_name)
            user.email.isEmpty() -> getStringResource(R.string.please_enter_email)
            else -> ""
        }
        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        repository.createNewUser(user, object : FirebaseDataListener<Any> {
            override fun onSuccess(result: Any) {
                response.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                response.value = Response(success = false, error = error)
            }
        })
        return response
    }
}