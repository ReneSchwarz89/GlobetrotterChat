package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirebaseService
import de.rs.globetrotterchat.android.data.remote.FirestoreService
import kotlinx.coroutines.launch

const val TAG = "MainViewModel"
class MainViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var authService: FirebaseService

    private val repository: Repository by lazy {
        Repository(FirestoreService(uid.toString()))
    }

    private val _uid = MutableLiveData<String?>()
    val uid: LiveData<String?> get() = _uid

    val profiles = repository.profiles

    fun setUid(uid:String){
        _uid.value = uid
    }


    fun logout(){
        viewModelScope.launch {
            authService.signOut()
        }
    }

    fun getProfiles() {
        viewModelScope.launch {
            repository.getAllProfiles()
            Log.e(TAG,"${profiles.value?.size}")
        }
    }

    fun setProfile(){
        viewModelScope.launch {
            val profile = Profile("","","")
            repository.setProfile(profile)
        }
    }
}
