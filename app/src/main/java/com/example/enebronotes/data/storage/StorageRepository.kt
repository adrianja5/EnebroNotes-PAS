package com.example.enebronotes.data.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

const val TAG = "STORAGE_REPOSITORY"

interface StorageRepository {
    fun putFile(uri: Uri): Flow<String>
}

class StorageRepositoryImpl(
    storage: FirebaseStorage
) : StorageRepository {


    private val storageRef = storage.reference


    override fun putFile(uri: Uri): Flow<String> = callbackFlow {
        //https://firebase.google.com/docs/storage/android/upload-files
        val name = UUID.randomUUID().toString()
        val filesRef = storageRef.child("files/$name")
        val uploadTask = filesRef.putFile(uri)
        val addOnSuccessListener1 = uploadTask.addOnSuccessListener {
            filesRef.downloadUrl.addOnSuccessListener {
                trySendBlocking(it.toString())
            }
        }
        awaitClose { addOnSuccessListener1.cancel() }
    }

}