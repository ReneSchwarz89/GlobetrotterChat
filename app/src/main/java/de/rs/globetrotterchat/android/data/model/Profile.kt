package de.rs.globetrotterchat.android.data.model


data class Profile(
    val nickname: String? = null,
    val profilePictureUrl: String? = null,
    var nativeLanguage: String? = null,
    val contacts: MutableList<Contact> = mutableListOf()
)

