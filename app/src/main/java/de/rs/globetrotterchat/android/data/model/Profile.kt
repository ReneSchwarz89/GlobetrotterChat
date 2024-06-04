package de.rs.globetrotterchat.android.data.model


data class Profile(
    val uid : String,
    val profilePictureUrl: String? = null,
    val nickname: String? = null,
    var nativeLanguage: String? = null,
)