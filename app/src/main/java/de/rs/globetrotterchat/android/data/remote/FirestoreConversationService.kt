package de.rs.globetrotterchat.android.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.model.Conversation
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

    suspend fun createConversation(conversationId: String, loggedInUid: String, otherUid: String){
        val conversation = Conversation(conversationId,participantsIds = listOf(loggedInUid, otherUid))
        val conversationRef = database.collection("Conversations").document(conversationId)
        conversationRef.set(conversation).await()
    }

    suspend fun loadConversations(): List<Conversation> {
        val conversationsRef = database.collection("Conversations")
        val querySnapshot = conversationsRef.whereArrayContains("participantsIds", uid).get().await()
        return querySnapshot.toObjects(Conversation::class.java)

    }


}


