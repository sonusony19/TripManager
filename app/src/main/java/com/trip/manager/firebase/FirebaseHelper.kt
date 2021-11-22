package com.trip.manager.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.trip.manager.listeners.FirebaseAuthListener
import com.trip.manager.utils.authError
import com.trip.manager.utils.databaseError

class FirebaseHelper {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance("https://trip-manager-e4ac1-default-rtdb.firebaseio.com/")

    fun signInUser(email: String, password: String, listener: FirebaseAuthListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                updateUserInDatabase(email, listener)
            } else {
                listener.onAuthFailure(task.exception?.localizedMessage ?: authError)
            }
        }
    }

    private fun signUpUser(email: String, password: String, listener: FirebaseAuthListener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                updateUserInDatabase(email, listener)
            } else {
                listener.onAuthFailure(task.exception?.localizedMessage ?: authError)
            }
        }
    }

    private fun updateUserInDatabase(email: String, listener: FirebaseAuthListener) {
        val userDatabase = database.getReference("Users").child(auth.currentUser!!.uid)
        val user = hashMapOf<String, Any>()
        user["uid"] = auth.currentUser!!.uid
        user["name"] = "Sonu Sony"
        user["email"] = email
        user["image"] = ""
        userDatabase.setValue(user).addOnCompleteListener { saveTask: Task<Void?> ->
            if (saveTask.isSuccessful) {
                listener.onAuthSuccess()
            } else {
                listener.onAuthFailure(saveTask.exception?.localizedMessage ?: databaseError)
            }
        }
    }
}