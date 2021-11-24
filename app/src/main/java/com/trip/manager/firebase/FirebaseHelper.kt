package com.trip.manager.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.trip.manager.listeners.FirebaseAuthListener
import com.trip.manager.ui.login.model.LoginRequest
import com.trip.manager.utils.authError
import com.trip.manager.utils.databaseError
import org.koin.core.KoinComponent

class FirebaseHelper : KoinComponent {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance("https://trip-manager-e4ac1-default-rtdb.firebaseio.com/")

    fun signInUser(loginRequest: LoginRequest, listener: FirebaseAuthListener) {
        auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password).addOnCompleteListener {
            if (it.isSuccessful) updateUserInDatabase(loginRequest.email, listener)
            else listener.onAuthFailure(it.exception?.localizedMessage ?: authError)
        }
    }

    private fun signUpUser(loginRequest: LoginRequest, listener: FirebaseAuthListener) {
        auth.createUserWithEmailAndPassword(loginRequest.email, loginRequest.password).addOnCompleteListener {
            if (it.isSuccessful) updateUserInDatabase(loginRequest.email, listener)
            else listener.onAuthFailure(it.exception?.localizedMessage ?: authError)
        }
    }

    private fun updateUserInDatabase(email: String, listener: FirebaseAuthListener) {
        val userDatabase = database.getReference("Users").child(auth.currentUser!!.uid)
        val user = hashMapOf<String, Any>()
        user["uid"] = auth.currentUser!!.uid
        user["name"] = "Sonu Sony"
        user["email"] = email
        user["image"] = ""
        userDatabase.setValue(user).addOnCompleteListener {
            if (it.isSuccessful) listener.onAuthSuccess()
            else listener.onAuthFailure(it.exception?.localizedMessage ?: databaseError)
        }
    }
}