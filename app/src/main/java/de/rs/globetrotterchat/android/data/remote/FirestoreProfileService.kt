package de.rs.globetrotterchat.android.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.model.Profile
import kotlinx.coroutines.tasks.await

class FirestoreProfileService(private val uid: String) {

    private val database = Firebase.firestore

    suspend fun setProfile(profile: Profile){
        database.collection("Profile").document(uid).set(profile).await()
    }

    suspend fun getAllProfiles(): List<Profile>{
        val result = database.collection("Profile").get().await()
        val profiles = result.toObjects(Profile::class.java)
        return profiles.filter { it.uid != uid }
    }

    suspend fun getProfile(uid: String = this.uid): Profile? {
        val result = database.collection("Profile").document(uid).get().await()
        return result.toObject(Profile::class.java)
    }

    suspend fun getNickName(uid: String): String {
        val profileRef = Firebase.firestore.collection("Profile").document(uid)
        val snapshot = profileRef.get().await()
        val profile = snapshot.toObject(Profile::class.java)
        return profile?.nickname ?: "Unbekannter Benutzer"
    }
}