package de.rs.globetrotterchat.android.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService

class Repository(private val firestoreService: FirestoreProfileService){

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    suspend fun setProfile(profile: Profile) {
        try {
            firestoreService.setProfile(profile)
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not set profile $profile: $e")
        }
    }

    suspend fun getAllProfiles(){
        try {
            _profiles.value = firestoreService.getAllProfiles()
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not load profile")
        }
    }
}