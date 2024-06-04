package de.rs.globetrotterchat.android.data.remote

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class StorageService {

    private val storageReference = FirebaseStorage.getInstance().reference

    fun uploadImage(imageUri: Uri, onSuccess: (Uri) -> Unit,onFailure: (Exception) -> Unit){
        val photoRef = storageReference.child("images/${imageUri.lastPathSegment}")
        photoRef.putFile(imageUri).addOnSuccessListener {
            photoRef.downloadUrl.addOnSuccessListener(onSuccess).addOnFailureListener(onFailure)
        }.addOnFailureListener(onFailure)
    }

    fun ImageView.loadImageFromStorage(imagePath: String) {
        val imageRef = storageReference.child(imagePath)
        Glide.with(this.context)
            .load(imageRef)
            .into(this)
    }
}

