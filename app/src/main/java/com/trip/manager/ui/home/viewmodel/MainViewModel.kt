package com.trip.manager.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.baseclasses.Response
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.network.MainRepository
import com.trip.manager.ui.trip.model.Trip

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    val tripData = MutableLiveData<Response<List<Trip>>>()

    fun getTrips() {
        repository.getTrips(object : FirebaseDataListener<List<Trip>> {
            override fun onSuccess(result: List<Trip>) {
                tripData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                tripData.value = Response(success = false, error = error)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeListener()
    }

    fun getCount() = repository.databaseAndListener.keys.size
}