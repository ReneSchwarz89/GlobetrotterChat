package de.rs.globetrotterchat.android.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.rs.globetrotterchat.android.data.model.Conversation
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreConversationService
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService

class Repository(
    private val firestoreProfileService: FirestoreProfileService,
    private var conversationService: FirestoreConversationService
) {

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
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not set profile $profile: $e")
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

    suspend fun getAllProfiles() {
        try {
            _profiles.value = firestoreProfileService.getAllProfiles()
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not load profiles")
        }
    }


    //Conversations

    private val _conversations = MutableLiveData<List<Conversation>>()
    val conversations: LiveData<List<Conversation>> get() = _conversations

    suspend fun checkAndCreateChatRoom(loggedInUid: String, otherUserId: String): String {
        try {
            val conversationId =
                conversationService.generateConversationId(loggedInUid, otherUserId)
            val conversationExists = conversationService.conversationExists(conversationId)
            if (!conversationExists) {
                conversationService.createConversation(conversationId, loggedInUid, otherUserId)
            }
            return conversationId
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not check or create chat room", e)
            throw e
        }
    }

    suspend fun loadConversations() {
        try {
            val conversations = conversationService.loadConversations()
            _conversations.postValue(conversations)
            println("Conversations loaded: ${conversations.size}")
        } catch (e: Exception) {
            println("Loading conversations for user with UID: ${conversations.value.toString()}")
            Log.e(Repository::class.simpleName, "AKTUELLER FEHLER ICH SUCHE WEITER!!! ",e)
        }
    }


}