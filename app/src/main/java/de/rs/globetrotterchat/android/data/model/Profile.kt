package de.rs.globetrotterchat.android.data.model


data class Profile(
    var uid: String? = null,
    val nickname: String? = null,
    var nativeLanguage: String? = null,
    var profilePictureUrl: String? = null
)