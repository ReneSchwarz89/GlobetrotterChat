package de.rs.globetrotterchat.android.data

import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.remote.FirebaseService

class Repository(private val firebaseService: FirebaseService) {

    suspend fun createUser(email: String, password: String): Boolean{
        return firebaseService.createUserWithEmailAndPassword(email,password)
    }

    suspend fun signInUser(email: String,password: String):Boolean{
        return firebaseService.signInWithEmailAndPassword(email,password)
    }

    fun signOut(){
        firebaseService.signOut()
    }

}