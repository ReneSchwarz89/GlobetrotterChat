package de.rs.globetrotterchat.android.data.model

data class Message(
    val id: String,
    val senderId: String,
    val senderNativelanguage: String? = null, // null optinal für die automatische erkennung beim registrieren... feature...
    val receiverId: String,
    val receiverNativeLanguage: String,
    val text: String,
    val translatedText: String? = null,
    val timeStamp: Long
)
