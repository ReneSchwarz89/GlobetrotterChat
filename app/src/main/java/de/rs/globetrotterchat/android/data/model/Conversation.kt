package de.rs.globetrotterchat.android.data.model

data class Conversation(
    val conversationId: String? = null,
    val participantsIds: List<String> = listOf(),
    var displayName: String? = null
)
