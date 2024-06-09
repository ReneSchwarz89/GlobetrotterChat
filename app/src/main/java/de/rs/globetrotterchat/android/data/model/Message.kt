package de.rs.globetrotterchat.android.data.model

data class Message(
    val id: String,
    val incoming: Boolean,
    val senderNativeLanguage: String? = null,
    val receiverNativeLanguage: String,
    val senderText: String,
    val translatedText: String? = null,

)
