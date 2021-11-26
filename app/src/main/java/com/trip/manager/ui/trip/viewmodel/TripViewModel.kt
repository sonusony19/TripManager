package com.trip.manager.ui.trip.viewmodel

import androidx.lifecycle.MutableLiveData
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.baseclasses.Response
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.network.TripRepository
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.model.Member
import com.trip.manager.ui.trip.model.Transaction
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.user.model.User
import com.trip.manager.utils.getStringResource
import java.util.*

class TripViewModel(private val repository: TripRepository) : BaseViewModel() {

    val userData = MutableLiveData<Response<List<User>>>()
    var tripData = MutableLiveData<Response<Trip>>()
    var essentialData = MutableLiveData<Response<List<Essential>>>()
    var memberData = MutableLiveData<Response<List<Member>>>()

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

    fun getMembers(tripId: String) {
        repository.getMembers(tripId, object : FirebaseDataListener<List<Member>> {
            override fun onSuccess(result: List<Member>) {
                memberData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                memberData.value = Response(success = false, error = error)
            }
        })
    }

    fun validateAndCreateTrip(trip: Trip, selectedUsers: ArrayList<User>): MutableLiveData<Response<Any>> {
        val error = when {
            trip.name.isNullOrEmpty() -> getStringResource(R.string.please_enter_trip_name)
            trip.members.isNullOrEmpty() -> getStringResource(R.string.please_select_at_least_one_member)
            else -> ""
        }
        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        trip.createdAt = Date().time
        repository.createNewTrip(trip, object : FirebaseDataListener<String> {
            override fun onSuccess(result: String) {
                addMembers(result, selectedUsers)
                response.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                response.value = Response(success = false, error = error)
            }
        })
        return response
    }

    private fun addMembers(tripId: String, selectedUsers: ArrayList<User>) {
        val members = arrayListOf<Member>()
        selectedUsers.forEach { members.add(Member(it.uid, it.name, it.image)) }
        repository.addMembers(tripId, members)
    }

    fun validateAndAddEssentials(tripId: String, essential: Essential): MutableLiveData<Response<Any>> {
        val error = when {
            essential.name.isEmpty() -> getStringResource(R.string.please_enter_essential_name)
            essential.responsible.isEmpty() -> getStringResource(R.string.please_enter_the_name_of_responsible_person)
            else -> ""
        }
        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        essential.createdAt = Date().time
        essential.updatedAt = essential.createdAt
        repository.addEssential(tripId, essential, object : FirebaseDataListener<String> {
            override fun onSuccess(result: String) {
                response.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                response.value = Response(success = false, error = error)
            }
        })
        return response
    }

    fun validateAndAddTransaction(tripId: String, transaction: Transaction, members: List<Member>): MutableLiveData<Response<Any>> {
        val error = when {
            transaction.title.isEmpty() -> getStringResource(R.string.please_enter_transaction_name)
            transaction.amount.isEmpty() || transaction.amount.toDouble() == 0.0 -> getStringResource(R.string.please_enter_the_amount)
            members.isEmpty() -> getStringResource(R.string.please_select_at_least_one_member)
            else -> ""
        }
        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        transaction.amount = (transaction.amount.toDouble() / members.size).toString()
        repository.addTransaction(tripId, transaction, members, object : FirebaseDataListener<String> {
            override fun onSuccess(result: String) {
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