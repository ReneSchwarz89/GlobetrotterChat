package de.rs.globetrotterchat.android.data.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val receiverId: String = "",
    val senderText: String = "",
    val senderNativeLanguage: String? = null,
    val receiverNativeLanguage: String? = null,
    val translatedText: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
