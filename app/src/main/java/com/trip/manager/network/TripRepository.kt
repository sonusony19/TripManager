package com.trip.manager.network

import com.trip.manager.baseclasses.BaseRepository
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.user.model.User
import com.trip.manager.utils.FirebasePaths

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

    fun createNewTrip(trip: Trip, listener: FirebaseDataListener<Any>) {
        var database = firebaseHelper.database.getReference(FirebasePaths.trips)
        database = database.push()
        trip.id = database.key ?: ""
        setObject(database, trip, listener)
    }
}