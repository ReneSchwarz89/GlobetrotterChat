package de.rs.globetrotterchat.android.data.model


data class User(
    val id: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
    var nativeLanguage: String,
    val contacts: MutableList<Contact> = mutableListOf()
)
