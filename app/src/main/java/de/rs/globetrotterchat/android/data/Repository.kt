package de.rs.globetrotterchat.android.data

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import de.rs.globetrotterchat.android.BuildConfig
import de.rs.globetrotterchat.android.data.model.Conversation
import de.rs.globetrotterchat.android.data.model.Message
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.data.remote.FirestoreConversationService
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService
import de.rs.globetrotterchat.android.data.remote.FirestoreStorageService
import de.rs.globetrotterchat.android.data.remote.GoogleTranslationApi
import de.rs.globetrotterchat.android.data.remote.GoogleTranslationService

class Repository(
    private val firestoreProfileService: FirestoreProfileService,
    private var conversationService: FirestoreConversationService,
    private val firestoreStorageService: FirestoreStorageService
) {

    //Profiles
    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?> get() = _userProfile

    /**
     * Setzt das Profil im Firestore und aktualisiert die LiveData-Variable `_userProfile`.
     *
     * Diese Funktion speichert das gegebene Profil im Firestore. Anschließend wird das Profil des aktuellen
     * Benutzers neu geladen und die LiveData-Variable `_userProfile` wird mit dem neuen Profil aktualisiert.
     * Im Erfolgsfall gibt die Funktion `true` zurück, im Fehlerfall `false`.
     *
     * @param profile Das Profil-Objekt, das gesetzt werden soll.
     * @return `true`, wenn das Profil erfolgreich gesetzt wurde, sonst `false`.
     */
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

    suspend fun uploadImageAndSaveUrl(imageUri: Uri) {
        try {
            val imageUrl = firestoreStorageService.uploadImage(imageUri)
            val loggedInUserProfile = firestoreProfileService.getProfile() != null
            if (loggedInUserProfile) {
                firestoreProfileService.saveImageUrl(imageUrl)
            } else{
                Log.e(Repository::class.simpleName, " No Profile found. Save Profile Data first")
            }
        }
        catch (e: Exception){
            Log.e(Repository::class.simpleName, "Could not upload image: $e")
        }
    }

    /**
     * Ruft das Profil des aktuellen Benutzers ab und weist es der LiveData-Variable `_userProfile` zu.
     *
     * Diese Funktion ruft den Service `firestoreProfileService` auf, um das Profil des aktuellen Benutzers zu laden.
     * Im Erfolgsfall wird das geladene Profil der LiveData-Variable `_userProfile` zugewiesen.
     * Im Fehlerfall wird eine Fehlermeldung im Log ausgegeben.
     */
    suspend fun getCurrentUserProfile() {
        try {
            _userProfile.value = firestoreProfileService.getProfile()
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not get profile")
        }
    }

    /**
     * Lädt alle Profile und weist sie der LiveData-Variable `_profiles` zu.
     *
     * Diese Funktion ruft den Service `firestoreProfileService` auf, um alle Profile zu laden.
     * Im Erfolgsfall werden die geladenen Profile der LiveData-Variable `_profiles` zugewiesen.
     * Im Fehlerfall wird eine Fehlermeldung im Log ausgegeben.
     */
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

    /**
     * Überprüft, ob eine Konversation existiert, und erstellt sie, falls nicht vorhanden.
     *
     * Diese Funktion generiert zuerst eine Konversations-ID basierend auf den Benutzer-IDs der Teilnehmer.
     * Dann wird überprüft, ob eine Konversation mit dieser ID bereits existiert. Falls nicht, wird eine neue
     * Konversation mit den gegebenen Benutzer-IDs und dem Anzeigenamen erstellt. Die generierte oder gefundene
     * Konversations-ID wird dann in das LiveData-Objekt `_currentConversationId` gesetzt und zurückgegeben.
     *
     * @param loggedInUid Die Benutzer-ID des angemeldeten Benutzers.
     * @param otherUserId Die Benutzer-ID des anderen Teilnehmers.
     * @param displayName Der Anzeigename, der in der neuen Konversation verwendet werden soll.
     * @return Die Konversations-ID der überprüften oder neu erstellten Konversation.
     * @throws Exception Wenn die Überprüfung oder Erstellung der Konversation fehlschlägt.
     */
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

    /**
     * Lädt die Konversationen für den angemeldeten Benutzer und aktualisiert die Anzeigenamen.
     *
     * Diese Funktion lädt zuerst alle Profile und dann alle Konversationen des angemeldeten Benutzers.
     * Für jede Konversation wird der Anzeigename des anderen Teilnehmers ermittelt und die Konversation
     * entsprechend aktualisiert. Die aktualisierten Konversationen werden dann in das LiveData-Objekt
     * `_conversations` gepostet. Im Fehlerfall wird der Stacktrace ausgegeben.
     *
     * @param loggedInUid Die eindeutige Benutzer-ID des angemeldeten Benutzers.
     */
    suspend fun loadConversationsForUser(loggedInUid: String) {
        try {
            val profilesList = firestoreProfileService.getAllProfiles()
            val conversationsList = conversationService.loadConversationsForUser()
            val conversationsWithCorrectName = conversationsList.map { conversation ->
                val otherUserId = conversation.participantsIds.first { it != loggedInUid }
                val displayName = profilesList.firstOrNull { it.uid == otherUserId }?.nickname ?: "Unbekannt"
                conversation.copy(displayName = displayName)
            }
            _conversations.postValue(conversationsWithCorrectName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Messages
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    /**
     * Fügt eine Nachricht zu einer bestimmten Konversation hinzu.
     *
     * Diese Funktion sucht zuerst die Konversation mit der gegebenen ID und ermittelt dann die Benutzer-ID
     * des anderen Teilnehmers, der nicht der angemeldete Benutzer ist. Wenn der andere Benutzer gefunden wird,
     * wird die Nachricht kopiert und die Empfänger-ID auf den anderen Benutzer gesetzt. Die neue Nachricht wird
     * dann zur Konversation hinzugefügt.
     *
     * @param conversationId Die ID der Konversation, zu der die Nachricht hinzugefügt werden soll.
     * @param message Das Nachrichtenobjekt, das hinzugefügt werden soll.
     * @param loggedInUid Die Benutzer-ID des angemeldeten Benutzers.
     */
    suspend fun addMessageToConversation(conversationId: String, message: Message, loggedInUid: String) {
        try {
            val conversation = conversationService.loadConversationsForUser()
                .find { it.conversationId == conversationId }
            val otherUserId = conversation?.participantsIds?.firstOrNull { it != loggedInUid }
            if (otherUserId != null) {
                val newMessage = message.copy(receiverId = otherUserId)
                conversationService.addMessageToConversation(conversationId, newMessage)
            }
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not add message to conversation.", e)
        }
    }

    /**
     * Lädt die Nachrichten für eine gegebene Konversations-ID.
     *
     * Diese Funktion ruft den Nachrichtenladeprozess für eine spezifische Konversation auf.
     * Die geladenen Nachrichten werden dann in das LiveData-Objekt `_messages` gepostet.
     * Im Fehlerfall wird eine Fehlermeldung im Log ausgegeben.
     *
     * @param conversationId Die ID der Konversation, deren Nachrichten geladen werden sollen.
     */
    suspend fun loadMessages(conversationId: String) {
        try {
            val messages = conversationService.loadMessages(conversationId)
            _messages.postValue(messages)
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Conversation could not load.", e)
        }
    }

    /**
     * Setzt die aktuelle Konversations-ID zurück.
     *
     * Diese Funktion setzt die LiveData-Variable `_currentConversationId` auf einen leeren String zurück.
     * Dies kann verwendet werden, um den Zustand zu bereinigen, wenn eine Konversation beendet oder verlassen wird.
     */
    fun resetCurrentConversationId() {
        try {
            _currentConversationId.value = ""
        } catch (e: Exception) {
            Log.e(Repository::class.simpleName, "Could not reset current ConversationId.", e)
        }
    }

    private var messagesListenerRegistration: ListenerRegistration? = null

    fun startListeningForMessages(conversationId: String) {
        messagesListenerRegistration = conversationService.listenForMessages(conversationId) { messages ->
            // Aktualisiere LiveData mit den neuen Nachrichten
            _messages.postValue(messages)
        }
    }

    fun stopListeningForMessages() {
        // Entferne den Listener, wenn er existiert
        messagesListenerRegistration?.remove()
        messagesListenerRegistration = null
    }


    //Translate
    private val googleTranslationService: GoogleTranslationService by lazy { GoogleTranslationApi.retrofitService }
    private val apikey = BuildConfig.apiKey

    /**
     * Ermittelt die Muttersprache des anderen Benutzers in einer Konversation.
     *
     * Diese Funktion sucht zuerst die Konversation mit der gegebenen ID und ermittelt dann die Benutzer-ID
     * des anderen Teilnehmers, der nicht der angemeldete Benutzer ist. Anschließend wird das Profil des anderen
     * Benutzers aus dem Firestore geladen, um dessen Muttersprache zu erhalten.
     *
     * @param conversationId Die ID der Konversation, in der die Sprache ermittelt werden soll.
     * @param loggedInUid Die Benutzer-ID des angemeldeten Benutzers.
     * @return Die Muttersprache des anderen Benutzers als String, oder null, wenn ein Fehler auftritt.
     */
    suspend fun getOtherUserNativeLanguage(conversationId: String, loggedInUid: String): String? {
        return try {
            val conversation = conversationService.loadConversationsForUser()
                .find { it.conversationId == conversationId }
            val otherUserId = conversation?.participantsIds?.firstOrNull { it != loggedInUid }
            val otherUserProfile = firestoreProfileService.getProfile(otherUserId!!)
            otherUserProfile?.nativeLanguage
        } catch (e: Exception) {
            Log.e("Repository", "Error occurred while fetching other user's native language: $e")
            null
        }
    }

    /**
     * Übersetzt den gegebenen Text in die Zielsprache.
     * @param senderText Der zu übersetzende Text.
     * @param targetLanguage Der Sprachcode der Zielsprache.
     * @return Der übersetzte Text.
     * @throws Exception Wenn die Übersetzung fehlschlägt.
     */
    suspend fun translateText(senderText: String, targetLanguage: String): String {
        try {
            val response = googleTranslationService.translateText(senderText, targetLanguage, apikey)
            if (response.isSuccessful && response.data.translations.isNotEmpty()) {
                return response.data.translations.first().translatedText
            } else {
                throw Exception("Translation failed: $response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("An error occurred during translation: ${e.message}")
        }


    }
}