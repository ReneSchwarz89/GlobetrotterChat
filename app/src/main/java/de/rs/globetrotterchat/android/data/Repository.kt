package de.rs.globetrotterchat.android.data

import android.util.Log
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreService

class Repository(private val firestoreService: FirestoreService){

    suspend fun setProfile(profile: Profile){
        try {
            firestoreService.setProfile(profile)
        } catch (e: Exception){
            Log.e(Repository::class.simpleName,"Could not set profile $profile: $e")
        }
    }
}