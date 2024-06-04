package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Profile
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
