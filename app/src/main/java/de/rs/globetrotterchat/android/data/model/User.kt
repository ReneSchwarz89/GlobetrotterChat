package de.rs.globetrotterchat.android.data.model

import de.rs.globetrotterchat.android.ui.friendsHub.ContactsManager

data class User(
    val id: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
    var nativeLanguage: String,
    val contacts: MutableList<Contact> = mutableListOf(),
    val contactsManager: ContactsManager = ContactsManager()
)
