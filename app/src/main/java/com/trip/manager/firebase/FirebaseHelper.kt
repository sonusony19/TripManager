package com.trip.manager.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.trip.manager.utils.TAG

class FirebaseHelper {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()

    fun showLog() {
        Log.d(TAG, "showLog: ")
    }
}