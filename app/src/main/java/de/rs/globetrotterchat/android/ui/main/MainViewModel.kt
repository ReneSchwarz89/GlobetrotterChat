package de.rs.globetrotterchat.android.ui.main

import android.app.Application
import android.net.Uri
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

    fun setProfile(nickname: String, nativeLanguage: String){
        viewModelScope.launch {
            val profile = Profile(uid = loggedInUid, nickname,nativeLanguage)
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

    /**
     * Sendet eine Nachricht in einer Konversation.
     *
     * Diese Funktion überprüft zuerst, ob die Muttersprache des Empfängers von der des Absenders abweicht.
     * Ist dies der Fall, wird der Text des Absenders in die Zielsprache übersetzt. Ansonsten wird der
     * Originaltext verwendet. Die Nachricht wird dann erstellt und zur Konversation hinzugefügt.
     * Abschließend werden die Nachrichten der Konversation neu geladen.
     *
     * @param senderText Der Text, den der Absender senden möchte.
     * @param conversationId Die ID der Konversation, in der die Nachricht gesendet werden soll.
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
                loadMessages(conversationId)
            } else {
                val message = Message(
                    senderId = loggedInUid,
                    senderText = senderText,
                    senderNativeLanguage = senderNativeLanguage,
                    receiverNativeLanguage = targetLanguage,
                    translatedText = senderText
                )
                repository.addMessageToConversation(conversationId, message, loggedInUid)
                loadMessages(conversationId)
            }
        }
    }

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            repository.loadMessages(conversationId)
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