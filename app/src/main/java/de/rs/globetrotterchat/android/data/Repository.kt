package de.rs.globetrotterchat.android.data

import android.util.Log
import de.rs.globetrotterchat.android.data.remote.FirebaseService

class Repository(private val firebaseService: FirebaseService) {

    val isLoggedIn = firebaseService.isLoggedIn

    suspend fun createUser(email: String, password: String): Boolean {
        try {
            firebaseService.createUserWithEmailAndPassword(email, password)
            return true
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not Sign Up User")
            return false
        }
    }

    suspend fun signInUser(email: String, password: String): Boolean {
        return firebaseService.signInWithEmailAndPassword(email, password)
    }

    fun signOut() {
        firebaseService.signOut()
    }

}