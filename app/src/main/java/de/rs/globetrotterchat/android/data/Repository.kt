package de.rs.globetrotterchat.android.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.rs.globetrotterchat.android.data.model.Contact
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirebaseService
import de.rs.globetrotterchat.android.data.remote.FirestoreService

class Repository(private val firestoreService: FirestoreService){

    private val firebaseService = FirebaseService()

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles


    suspend fun setProfile(profile: Profile){
        try {
            firestoreService.setProfile(profile)
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not set profile $profile: $e")
        }
    }

    suspend fun getAllProfiles(): Boolean {
        try {
            val uid =firebaseService.userId ?: return false
            val firestoreService = FirestoreService(uid)
            _profiles.value = firestoreService.getAllProfiles()
            return true
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not load profile")
            return false
        }
    }

}