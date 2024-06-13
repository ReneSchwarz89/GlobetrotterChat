package de.rs.globetrotterchat.android.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.rs.globetrotterchat.android.data.model.Conversation
import de.rs.globetrotterchat.android.data.model.Message
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreConversationService
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService

class Repository(
    private val firestoreProfileService: FirestoreProfileService,
    private var conversationService: FirestoreConversationService
) {

    //Profiles

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

    private val _currentConversationId = MutableLiveData<String>()
    val currentConversationId: LiveData<String> get() = _currentConversationId

    suspend fun checkAndCreateConversation(loggedInUid: String, otherUserId: String, displayName: String): String {
        try {
            val conversationId = conversationService.generateConversationId(loggedInUid, otherUserId)
            val conversationExists = conversationService.conversationExists(conversationId)
            if (!conversationExists) {
                conversationService.createConversation(conversationId, loggedInUid, otherUserId, displayName)
            }
            _currentConversationId.value = conversationId
            return conversationId
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not check or create chat room", e)
            throw e
        }
    }

    suspend fun loadConversationsForUser(loggedInUid: String) {
        val profilesList = firestoreProfileService.getAllProfiles()
        val conversationsList = conversationService.loadConversationsForUser()
        val conversationsWithCorrectName = conversationsList.map { conversation ->
            val otherUserId = conversation.participantsIds.first { it != loggedInUid }
            val displayName = profilesList.firstOrNull { it.uid == otherUserId }?.nickname ?: "Unbekannt"
            conversation.copy(displayName = displayName)
        }
        _conversations.postValue(conversationsWithCorrectName)
    }

    // Messages
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages


    suspend fun addMessageToConversation(conversationId: String, message: Message, loggedInUid: String){
        try {
            val conversation = conversationService.loadConversationsForUser().find { it.conversationId == conversationId}
            val otherUserId = conversation?.participantsIds?.firstOrNull { it != loggedInUid }
            if (otherUserId != null) {
                val newMessage = message.copy(receiverId = otherUserId)
                conversationService.addMessageToConversation(conversationId, newMessage)
            }
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not add message to conversation.", e)
        }
    }

    suspend fun loadMessages(conversationId: String) {
        try {
            val messages = conversationService.loadMessages(conversationId)
            _messages.postValue(messages)
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Conversation could not load.", e)
        }
    }

    fun resetCurrentConversationId() {
        try {
            _currentConversationId.value = ""
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not reset current ConversationId.", e)
        }
    }

}