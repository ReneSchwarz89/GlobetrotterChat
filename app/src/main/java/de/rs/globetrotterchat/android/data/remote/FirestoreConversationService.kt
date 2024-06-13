package de.rs.globetrotterchat.android.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.model.Conversation
import de.rs.globetrotterchat.android.data.model.Message
import kotlinx.coroutines.tasks.await

class FirestoreConversationService(private val uid: String) {

    private val database = Firebase.firestore

    fun generateConversationId(loggedInUid: String, uid2: String): String {
        return listOf(loggedInUid, uid2).sorted().joinToString("-")
    }

    suspend fun conversationExists(conversationId: String): Boolean{
        val conversationRef = database.collection("Conversations").document(conversationId)
        return conversationRef.get().await().exists()
    }

    suspend fun createConversation(conversationId: String, loggedInUid: String, otherUid: String, displayName: String){
        val conversation = Conversation(conversationId,participantsIds = listOf(loggedInUid, otherUid),displayName)
        val conversationRef = database.collection("Conversations").document(conversationId)
        conversationRef.set(conversation).await()
    }

    suspend fun loadConversationsForUser(): List<Conversation> {
        val conversationsRef = database.collection("Conversations")
        val querySnapshot = conversationsRef.whereArrayContains("participantsIds", uid).get().await()
        return querySnapshot.toObjects(Conversation::class.java)
    }

    suspend fun addMessageToConversation(conversationId: String, message: Message){
        val messagesRef = database.collection("Conversations").document(conversationId).collection("Messages")
        messagesRef.add(message).await()
    }

    suspend fun loadMessages(conversationId: String): List<Message> {
        val messagesRef = database.collection("Conversations").document(conversationId).collection("Messages")
        val querySnapshot = messagesRef.get().await()
        return querySnapshot.documents.mapNotNull { it.toObject(Message::class.java) }

    }
}



