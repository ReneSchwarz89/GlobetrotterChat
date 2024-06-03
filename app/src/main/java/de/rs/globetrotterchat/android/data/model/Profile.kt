package de.rs.globetrotterchat.android.data.model


data class Profile(
    val profilePictureUrl: String? = null,
    val nickname: String? = null,
    var nativeLanguage: String? = null,
    val contacts: MutableList<Contact> = mutableListOf(),
    val conversations: List<String> = emptyList()
)