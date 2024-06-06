package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val loggedInUid = Firebase.auth.uid!!
    private val firestoreService = FirestoreProfileService(loggedInUid)
    private val repository = Repository(firestoreService)

    val profiles = repository.profiles
    val userProfile = repository.userProfile

    init {
        viewModelScope.launch {
            repository.getCurrentUserProfile()
        }
    }

    fun setProfile(nickname: String, nativeLanguage: String){
        viewModelScope.launch {
            val profile = Profile(nickname,nativeLanguage)
            repository.setProfile(profile)
        }
    }

    fun getProfiles() {
        viewModelScope.launch {
            repository.getAllProfiles()
        }
    }
}