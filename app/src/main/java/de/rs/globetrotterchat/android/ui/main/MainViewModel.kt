package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirebaseService
import de.rs.globetrotterchat.android.data.remote.FirestoreService
import kotlinx.coroutines.launch


class MainViewModel(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {


    val profiles = repository.profiles

    fun getProfiles() {
        viewModelScope.launch {
            repository.getAllProfiles()
        }
    }

    fun setProfile(){
        viewModelScope.launch {
            val profile = Profile("","Zeus","")
            repository.setProfile(profile)
        }
    }
}
