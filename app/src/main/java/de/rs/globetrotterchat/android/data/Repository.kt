package de.rs.globetrotterchat.android.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService

class Repository(private val firestoreProfileService: FirestoreProfileService){

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?> get() = _userProfile


    suspend fun setProfile(profile: Profile): Boolean {
        try {
            firestoreProfileService.setProfile(profile)
            getCurrentUserProfile()
            _userProfile.postValue(profile)
            return true
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not set profile $profile: $e")
            return false
        }
    }
    suspend fun getCurrentUserProfile() {
        try {
            _userProfile.value = firestoreProfileService.getProfile()
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not get profile")
        }
    }

    suspend fun getAllProfiles(){
        try {
            _profiles.value = firestoreProfileService.getAllProfiles()
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not load profiles")
        }
    }

}