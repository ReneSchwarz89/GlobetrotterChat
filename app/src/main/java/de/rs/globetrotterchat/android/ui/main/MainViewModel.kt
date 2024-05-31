package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirebaseService
import de.rs.globetrotterchat.android.data.remote.FirestoreService
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var authService: FirebaseService

    private val _uid = MutableLiveData<String?>()
    val uid: LiveData<String?> get() = _uid

    private val repository: Repository by lazy {
        Repository(FirestoreService(uid.toString()))
    }

    init {

    }



    fun setUid(uid:String){
        _uid.value = uid
    }

    private fun clearUid(){
        _uid.value = null
    }

    fun logout(){
        viewModelScope.launch {
            authService.signOut()
            clearUid()
        }
    }

    fun setProfile(){
        viewModelScope.launch {
            val profile = Profile("irgendwas","","")
            repository.setProfile(profile)
        }
    }
}
