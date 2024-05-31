package de.rs.globetrotterchat.android.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.model.Profile
import kotlinx.coroutines.tasks.await

class FirestoreService(private val uid: String) {

    private val database = Firebase.firestore

    suspend fun setProfile(profile: Profile){
        database.collection("Profile").document(uid).set(profile).await()
    }


}