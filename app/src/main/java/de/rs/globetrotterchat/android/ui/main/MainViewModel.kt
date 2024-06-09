package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirebaseConversationService
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val loggedInUid = Firebase.auth.uid!!
    private val firestoreProfileService = FirestoreProfileService(loggedInUid)
    private val firebaseConversationService = FirebaseConversationService(loggedInUid)
    private val repository = Repository(firestoreProfileService,firebaseConversationService)

    val profiles = repository.profiles
    val userProfile = repository.userProfile
    val conversation = repository.conversations

    init {
        viewModelScope.launch {
            repository.getCurrentUserProfile()
        }
    }

    fun setProfile(nickname: String, nativeLanguage: String){
        viewModelScope.launch {
            val profile = Profile(uid = loggedInUid, nickname,nativeLanguage)
            repository.setProfile(profile)
        }
    }

    fun getProfiles() {
        viewModelScope.launch {
            repository.getAllProfiles()
        }
    }

    fun createOrGetChatRoom(otherUserId: String) {
        viewModelScope.launch {
            repository.checkAndCreateChatRoom(loggedInUid, otherUserId)
        }
    }

    fun getConversationsForUser(){
        viewModelScope.launch {
            repository.getConversationsForUser()
        }
    }

    fun loadConversation() {
        viewModelScope.launch {

        }
    }

}