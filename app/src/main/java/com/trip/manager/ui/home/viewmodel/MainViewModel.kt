package com.trip.manager.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.baseclasses.Response
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.network.MainRepository
import com.trip.manager.ui.home.model.Trips
import com.trip.manager.ui.trip.model.Trip
import java.util.*

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    val tripData = MutableLiveData<Response<Trips>>()

    fun getTrips() {
        val current = Date().time
        repository.getTrips(object : FirebaseDataListener<List<Trip>> {
            override fun onSuccess(result: List<Trip>) {
                val response = Response<Trips>(success = true, data = Trips())
                result.forEach {
                    when {
                        current < it.startAt!! -> response.data?.upcomingTrips?.add(it)
                        current > it.endAt!! -> response.data?.pastTrips?.add(it)
                        else -> response.data?.currentTrips?.add(it)
                    }
                }
                tripData.value = response
            }

            override fun onFailure(error: String) {
                tripData.value = Response(success = false, error = error)
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        removeListener()
    }

    fun removeListener() {
        repository.removeListener()
    }
}