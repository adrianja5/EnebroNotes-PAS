package com.example.enebronotes.data.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

const val TAG = "AUTH_REPOSITORY"

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun getCurrentUserFlow(): Flow<FirebaseUser?>
    fun signIn(email: String, password: String): Flow<FirebaseUser>
    fun signup(name: String, email: String, password: String): Flow<FirebaseUser>
    fun logout()
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    @OptIn(FlowPreview::class)
    override fun getCurrentUserFlow(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySendBlocking(it.currentUser)
            Log.e(TAG, "currentUser " + it.currentUser?.email.toString())
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.debounce(200) // https://stackoverflow.com/a/57338951

    override fun signIn(email: String, password: String): Flow<FirebaseUser> = flow {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun signup(name: String, email: String, password: String): Flow<FirebaseUser> = flow {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            emit(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}