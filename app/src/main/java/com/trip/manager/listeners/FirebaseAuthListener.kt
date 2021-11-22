package com.trip.manager.listeners

interface FirebaseAuthListener {
    fun onAuthSuccess()
    fun onAuthFailure(error: String)
}