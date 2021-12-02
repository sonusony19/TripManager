package com.trip.manager.listeners

interface FirebaseAuthListener {
    fun onAuthSuccess()
    fun onAuthFailure(error: String)
}

interface FirebaseDataListener<T> {
    fun onSuccess(result: T)
    fun onFailure(error: String)
}