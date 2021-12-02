package com.trip.manager.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.baseclasses.Response
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.network.UserRepository
import com.trip.manager.ui.user.model.User
import com.trip.manager.utils.getStringResource
import java.util.*

class UserViewModel(private val repository: UserRepository) : BaseViewModel() {

    val userData = MutableLiveData<Response<List<User>>>()


    fun getUsers() {
        loading.value = true
        repository.getUsers(object : FirebaseDataListener<List<User>> {
            override fun onSuccess(result: List<User>) {
                userData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                userData.value = Response(success = false, error = error)
            }
        })
    }

    fun validateAndCreateUser(user: User): MutableLiveData<Response<Any>> {
        user.email = user.email.lowercase(Locale.ENGLISH)
        val error = when {
            user.name.isEmpty() -> getStringResource(R.string.please_enter_the_name)
            user.email.isEmpty() -> getStringResource(R.string.please_enter_email)
            userData.value?.data?.any { it.email == user.email } == true -> getStringResource(R.string.this_email_already_exists)
            else -> ""
        }

        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        repository.createNewUser(user, object : FirebaseDataListener<String> {
            override fun onSuccess(result: String) {
                response.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                response.value = Response(success = false, error = error)
            }
        })
        return response
    }
}