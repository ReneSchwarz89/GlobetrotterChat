package de.rs.globetrotterchat.android.data.model

data class Conversation(
    val id: String,
    val participants: List<String>,
    val messages: MutableList<Message>
)
