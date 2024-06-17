package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.model.Message
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreConversationService
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val loggedInUid = Firebase.auth.uid!!
    private val firestoreProfileService = FirestoreProfileService(loggedInUid)
    private val firebaseConversationService = FirestoreConversationService(loggedInUid)
    private val repository = Repository(firestoreProfileService,firebaseConversationService)

    val profiles = repository.profiles
    val userProfile = repository.userProfile
    val conversation = repository.conversations
    val messages = repository.messages
    val currentConversationId = repository.currentConversationId

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

    fun sendMessage(senderText: String, conversationId: String) {
        viewModelScope.launch {
            // Zuerst die Zielsprache des Empfängers abrufen
            val targetLanguage = repository.getOtherUserNativeLanguage(conversationId, loggedInUid)

            // Überprüfe, ob die Zielsprache erhalten wurde
            if (targetLanguage != null) {
                // Übersetze den Text
                val translatedText = repository.translateText(senderText, targetLanguage)

                // Erstelle eine neue Nachricht mit dem übersetzten Text
                val message = Message(
                    senderId = loggedInUid,
                    senderText = senderText,
                    receiverNativeLanguage = targetLanguage,
                    translatedText = translatedText
                )

                // Füge die Nachricht zur Konversation hinzu
                repository.addMessageToConversation(conversationId, message, loggedInUid)
                loadMessages(conversationId)
            } else {
                // Handle den Fall, dass keine Zielsprache gefunden wurde
                Log.e("MainViewModel", "Keine Zielsprache gefunden.")
            }
        }
    }

    fun resetCurrentConversationId() {
        viewModelScope.launch {
            repository.resetCurrentConversationId()
        }
    }

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            repository.loadMessages(conversationId)
        }
    }
}