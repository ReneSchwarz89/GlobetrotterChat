package de.rs.globetrotterchat.android.ui.friendsHub

import de.rs.globetrotterchat.android.data.model.Contact

class ContactsManager {
    val contacts: MutableList<Contact> = mutableListOf()

    fun addContact(contact: Contact) {
        contacts.add(contact)
    }

    fun removeContact(contactId: String) {
        contacts.removeAll { it.id == contactId }
    }
}
