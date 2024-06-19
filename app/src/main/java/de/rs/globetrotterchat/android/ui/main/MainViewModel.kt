package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Message
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreConversationService
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService
import de.rs.globetrotterchat.android.data.remote.FirestoreStorageService
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val loggedInUid = Firebase.auth.uid!!
    private val firestoreProfileService = FirestoreProfileService(loggedInUid)
    private val firebaseConversationService = FirestoreConversationService(loggedInUid)
    private val firestoreStorageService = FirestoreStorageService(loggedInUid)
    private val repository = Repository(firestoreProfileService,firebaseConversationService,firestoreStorageService)

    val profiles = repository.profiles
    val userProfile = repository.userProfile
    val conversation = repository.conversations
    val messages = repository.messages
    val currentConversationId = repository.currentConversationId

    init {
        loadUserProfile()
    }

    //Profile

    fun setProfile(nickname: String, nativeLanguage: String){
        viewModelScope.launch {
            val profile = Profile(uid = loggedInUid, nickname, nativeLanguage, userProfile.value?.profilePictureUrl)
            repository.setProfile(profile)
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            repository.getCurrentUserProfile()
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            repository.uploadImageAndSaveUrl(imageUri)
        }
    }

    fun getProfiles() {
        viewModelScope.launch {
            repository.getAllProfiles()
        }
    }

    //Conversations

    fun createOrGetConversation(otherUserId: String, displayName: String) {
        viewModelScope.launch {
            repository.checkAndCreateConversation(loggedInUid, otherUserId, displayName)
        }
    }

    fun loadConversations(){
        viewModelScope.launch {
            repository.loadConversationsForUser(loggedInUid)
        }
    }

    fun resetCurrentConversationId() {
        viewModelScope.launch {
            repository.resetCurrentConversationId()
        }
    }

    //Messages

    /**
     * Sendet eine Nachricht in einer Konversation.
     *
     * Diese Funktion sendet eine Nachricht von einem Sender an einen Empfänger innerhalb einer Konversation.
     * Wenn die Muttersprache des Senders und die des Empfängers unterschiedlich sind, wird der Text übersetzt.
     * Die übersetzte Nachricht wird dann zur Konversation hinzugefügt.
     * Falls die Sprachen identisch sind, wird der Originaltext gesendet.
     *
     * @param senderText Der Text der Nachricht, die gesendet werden soll.
     * @param conversationId Die ID der Konversation, zu der die Nachricht hinzugefügt werden soll.
     */
    fun sendMessage(senderText: String, conversationId: String) {
        viewModelScope.launch {
            val senderNativeLanguage = userProfile.value?.nativeLanguage
            val targetLanguage = repository.getOtherUserNativeLanguage(conversationId, loggedInUid)
            if (targetLanguage != null && senderNativeLanguage != null && targetLanguage != senderNativeLanguage) {
                val translatedText = repository.translateText(senderText, targetLanguage)
                val message = Message(
                    senderId = loggedInUid,
                    senderText = senderText,
                    senderNativeLanguage = senderNativeLanguage,
                    receiverNativeLanguage = targetLanguage,
                    translatedText = translatedText
                )
                repository.addMessageToConversation(conversationId, message, loggedInUid)
            } else {
                val message = Message(
                    senderId = loggedInUid,
                    senderText = senderText,
                    senderNativeLanguage = senderNativeLanguage,
                    receiverNativeLanguage = targetLanguage,
                    translatedText = senderText
                )
                repository.addMessageToConversation(conversationId, message, loggedInUid)
            }
        }
    }

    fun startListeningForMessages(conversationId: String) {
        viewModelScope.launch {
            repository.startListeningForMessages(conversationId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopListeningForMessages()
    }
}