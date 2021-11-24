package com.trip.manager.network

import com.trip.manager.baseclasses.BaseRepository
import com.trip.manager.firebase.FirebaseHelper
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.ui.user.model.User

class UserRepository(private val firebaseHelper: FirebaseHelper) : BaseRepository() {

    fun createNewUser(user: User, listener: FirebaseDataListener<Any>) {
        var database = firebaseHelper.database.getReference("Users")
        database = database.push()
        user.uid = database.key ?: ""
        setObject(database, user, listener)
    }
}