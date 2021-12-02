package com.trip.manager.ui.trip.viewmodel

import androidx.lifecycle.LiveData
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
import com.trip.manager.utils.getDeviceModel
import com.trip.manager.utils.getStringResource
import java.util.*

class TripViewModel(private val repository: TripRepository) : BaseViewModel() {

    val userData = MutableLiveData<Response<List<User>>>()
    var tripData = MutableLiveData<Response<Trip>>()
    var essentialData = MutableLiveData<Response<List<Essential>>>()
    var memberData = MutableLiveData<Response<List<Member>>>()
    var transactionData = MutableLiveData<Response<List<Transaction>>>()

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

    fun getTransaction(tripId: String, memberId: String) {
        loading.value = true
        repository.getTransactions(tripId, memberId, object : FirebaseDataListener<List<Transaction>> {
            override fun onSuccess(result: List<Transaction>) {
                transactionData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                transactionData.value = Response(success = false, error = error)
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

    fun getEssentialDetails(tripId: String, essentialId: String): LiveData<Response<Essential>> {
        val essentialData = MutableLiveData<Response<Essential>>()
        repository.getEssentialDetails(tripId, essentialId, object : FirebaseDataListener<Essential> {
            override fun onSuccess(result: Essential) {
                essentialData.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                essentialData.value = Response(success = false, error = error)
            }
        })
        return essentialData
    }

    fun getMembers(tripId: String, once: Boolean = false) {
        loading.value = true
        repository.getMembers(tripId, once, object : FirebaseDataListener<List<Member>> {
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
            trip.name.isEmpty() -> getStringResource(R.string.please_enter_trip_name)
            trip.members.isNullOrEmpty() -> getStringResource(R.string.please_select_at_least_one_member)
            trip.startAt == null -> getStringResource(R.string.please_select_start_date)
            trip.endAt == null -> getStringResource(R.string.please_select_end_date)
            else -> ""
        }
        if (error.isNotEmpty()) {
            return MutableLiveData(Response(success = false, error = error))
        }
        val response = MutableLiveData<Response<Any>>()
        trip.createdAt = Date().time
        trip.createdBy = getDeviceModel()
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
        essential.updatedBy = getDeviceModel()
        essential.createdBy = getDeviceModel()
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

    fun validateAndAddTransaction(tripId: String, transaction: Transaction, members: List<Member>): LiveData<Response<Any>> {
        val error = when {
            transaction.title.isEmpty() -> getStringResource(R.string.please_enter_transaction_name)
            transaction.amount.isEmpty() || transaction.amount.toDoubleOrNull() == null || transaction.amount.toDoubleOrNull() == 0.0 -> getStringResource(R.string.please_enter_the_amount)
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

    fun deleteEssential(tripID: String, essentialID: String): LiveData<Response<Any>> {
        loading.value = true
        val response = MutableLiveData<Response<Any>>()
        repository.deleteEssential(tripID, essentialID, object : FirebaseDataListener<Any> {
            override fun onSuccess(result: Any) {
                response.value = Response(success = true, data = result)
            }

            override fun onFailure(error: String) {
                response.value = Response(success = false, error = error)
            }
        })
        return response
    }

    fun validateAndSaveEssential(tripId: String, essential: Essential): LiveData<Response<String>> {
        val response = MutableLiveData<Response<String>>()
        val error = when {
            essential.name.isEmpty() -> getStringResource(R.string.please_enter_essential_name)
            essential.handledAt != null && essential.handledBy.isEmpty() -> getStringResource(R.string.please_tell_you_handled_it)
            else -> ""
        }
        if (error.isNotEmpty()) {
            response.value = Response(success = false, error = error)
            return response
        }
        loading.value = true
        repository.updateEssential(tripId, essential, object : FirebaseDataListener<String> {
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