package de.rs.globetrotterchat.android.data.model

data class Contact(
    val id: String,
    val username: String,
    val profilePictureUrl: String? = null,
    val nativeLanguage: String,
    val conversationIds: MutableList<String> = mutableListOf()

)
