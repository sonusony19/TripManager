package com.trip.manager.network

import com.trip.manager.baseclasses.BaseRepository
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.model.Member
import com.trip.manager.ui.trip.model.Transaction
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.user.model.User
import com.trip.manager.utils.FirebasePaths
import com.trip.manager.utils.TransactionType
import com.trip.manager.utils.getDeviceModel
import java.util.*

class TripRepository(private val firebaseHelper: FirebaseHelper) : BaseRepository() {

    fun getUsers(listener: FirebaseDataListener<List<User>>) {
        listenToList(firebaseHelper.database.getReference(FirebasePaths.users), User::class.java, listener)
    }

    fun getTripDetails(id: String, listener: FirebaseDataListener<Trip>) {
        listenToObject(firebaseHelper.database.getReference(FirebasePaths.trips).child(id), Trip::class.java, listener)
    }

    fun getEssentials(tripId: String, listener: FirebaseDataListener<List<Essential>>) {
        listenToList(firebaseHelper.database.getReference(FirebasePaths.essentials).child(tripId), Essential::class.java, listener)
    }

    fun getMembers(tripId: String, once: Boolean, listener: FirebaseDataListener<List<Member>>) {
        listenToList(firebaseHelper.database.getReference(FirebasePaths.members).child(tripId), Member::class.java, listener, once)
    }

    fun createNewTrip(trip: Trip, listener: FirebaseDataListener<String>) {
        var database = firebaseHelper.database.getReference(FirebasePaths.trips)
        database = database.push()
        trip.id = database.key ?: ""
        setObject(database, trip, listener)
    }

    fun addMembers(tripId: String, members: ArrayList<Member>) {
        val database = firebaseHelper.database.getReference(FirebasePaths.members).child(tripId)
        members.forEach {
            val memberDatabase = database.child(it.id)
            setObject(memberDatabase, it)
        }
    }

    fun addEssential(tripId: String, essential: Essential, listener: FirebaseDataListener<String>) {
        var database = firebaseHelper.database.getReference(FirebasePaths.essentials).child(tripId)
        database = database.push()
        essential.id = database.key ?: ""
        setObject(database, essential, listener)
    }

    fun addTransaction(tripId: String, transaction: Transaction, members: List<Member>, listener: FirebaseDataListener<String>) {
        val database = firebaseHelper.database.getReference(FirebasePaths.transactions).child(tripId)
        members.forEach {
            var childDatabase = database.child(it.id)
            childDatabase = childDatabase.push()
            transaction.id = childDatabase.key ?: ""
            transaction.createdAt = Date().time
            transaction.createdBy = getDeviceModel()
            setObject(childDatabase, transaction, listener)
        }
        val amount = if (transaction.type == TransactionType.CREDIT) transaction.amount.toDouble() else -transaction.amount.toDouble()
        updateBalanceForMembers(tripId, amount, members)
    }

    private fun updateBalanceForMembers(tripId: String, amount: Double, members: List<Member>) {
        val database = firebaseHelper.database.getReference(FirebasePaths.members).child(tripId)
        members.forEach {
            val childDatabase = database.child(it.id)
            val data = hashMapOf<String, Any>()
            data["balance"] = it.balance + amount
            updateObject(childDatabase, data)
        }
    }

    fun getEssentialDetails(tripId: String, essentialId: String, listener: FirebaseDataListener<Essential>) {
        listenToObject(firebaseHelper.database.getReference(FirebasePaths.essentials).child(tripId).child(essentialId), Essential::class.java, listener, true)
    }

    fun deleteEssential(tripID: String, essentialID: String, listener: FirebaseDataListener<Any>) {
        val database = firebaseHelper.database.getReference(FirebasePaths.essentials).child(tripID).child(essentialID)
        removeObject(database, listener)
    }

    fun updateEssential(tripId: String, essential: Essential, firebaseDataListener: FirebaseDataListener<String>) {
        val database = firebaseHelper.database.getReference(FirebasePaths.essentials).child(tripId).child(essential.id)
        val data = hashMapOf<String, Any>()
        data["name"] = essential.name
        data["updatedAt"] = Date().time
        data["updatedBy"] = getDeviceModel()
        if (essential.handledAt != null) {
            data["handledAt"] = essential.handledAt!!
            data["handledBy"] = essential.handledBy
        }
        updateObject(database, data, firebaseDataListener)
    }

    fun getTransactions(tripId: String, memberId: String, listener: FirebaseDataListener<List<Transaction>>) {
        val database = firebaseHelper.database.getReference(FirebasePaths.transactions).child(tripId).child(memberId)
        listenToList(database, Transaction::class.java, listener)
    }
}