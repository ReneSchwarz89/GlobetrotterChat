package de.rs.globetrotterchat.android.data.model

data class Conversation(
    val conversationId: String? = null,
    val conversationName: String? = null,
    val participantsIds: List<String>,
    val messages: MutableList<Message> = mutableListOf()
)
