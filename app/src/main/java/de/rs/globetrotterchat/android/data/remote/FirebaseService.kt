package de.rs.globetrotterchat.android.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseService {

    val userId: String? get() = Firebase.auth.uid

    suspend fun createUserWithEmailAndPassword(email:String,password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email,password).await()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email,password).await()
    }

    fun signOut(){ Firebase.auth.signOut() }
}