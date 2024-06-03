package de.rs.globetrotterchat.android.data.model

data class Contact(
    val id: String,
    val nickname: String,
    val profilePictureUrl: String? = null,
    val conversationId: String
)
