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

    private var nickname = ""
    private var nativeLanguage = ""
    private var profilePictureUrl = ""


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

    fun setProfileImage(profileImage: String?){
        if (profileImage != null) {
            this.profilePictureUrl = profileImage
        }
    }

    fun setNickname(nickname: String){
        this.nickname = nickname
    }

    fun setNativeLanguage(nativeLanguage: String){
        this.nativeLanguage = nativeLanguage
    }
}
