package com.trip.manager.ui.trip.viewmodel

import androidx.lifecycle.MutableLiveData
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.baseclasses.Response
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.network.TripRepository
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.user.model.User
import com.trip.manager.utils.getStringResource

class TripViewModel(private val repository: TripRepository) : BaseViewModel() {

    val userData = MutableLiveData<Response<List<User>>>()
    var tripData = MutableLiveData<Response<Trip>>()
    var essentialData = MutableLiveData<Response<List<Essential>>>()

    fun getTripDetails(id: String) {
        repository.getTripDetails(id, object : FirebaseDataListener<Trip> {
            override fun onSuccess(result: Trip) {
                tripData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                tripData.value = Response(success = false, error = error)
            }
        })
    }

    fun getUsers() {
        repository.getUsers(object : FirebaseDataListener<List<User>> {
            override fun onSuccess(result: List<User>) {
                userData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                userData.value = Response(success = false, error = error)
            }
        })
    }

    fun getEssentials(tripId: String) {
        repository.getEssentials(tripId, object : FirebaseDataListener<List<Essential>> {
            override fun onSuccess(result: List<Essential>) {
                essentialData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                essentialData.value = Response(success = false, error = error)
            }
        })
    }

    fun validateAndCreateTrip(trip: Trip): MutableLiveData<Response<Any>> {
        val error = when {
            trip.name.isNullOrEmpty() -> getStringResource(R.string.please_enter_trip_name)
            trip.members.isNullOrEmpty() -> getStringResource(R.string.please_select_at_least_one_member)
            else -> ""
        }
        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        repository.createNewTrip(trip, object : FirebaseDataListener<Any> {
            override fun onSuccess(result: Any) {
                response.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                response.value = Response(success = false, error = error)
            }
        })
        return response
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeListener()
    }
}