package de.rs.globetrotterchat.android.data.remote

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirestoreStorageService(private val uid: String) {

    private val storageReference = FirebaseStorage.getInstance().reference

    suspend fun uploadImage(imageUri: Uri): String {
        try {
            val photoRef = storageReference.child("user_images/$uid/${imageUri.lastPathSegment}")
            photoRef.putFile(imageUri).await()
            val downloadUrl = photoRef.downloadUrl.await()
            return downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("FirestoreStorageService", "Fehler beim Hochladen des Bildes: $e")
            throw e // Oder handle den Fehler entsprechend
        }
    }
}

