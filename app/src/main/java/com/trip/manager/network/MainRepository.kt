package com.trip.manager.network

import com.trip.manager.baseclasses.BaseRepository
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.ui.trip.model.Trip

class MainRepository(private val firebaseHelper: FirebaseHelper) : BaseRepository() {

    fun getTrips(firebaseDataListener: FirebaseDataListener<List<Trip>>) {
        listenToList(firebaseHelper.database.getReference("Trips"), Trip::class.java, firebaseDataListener)
    }
}