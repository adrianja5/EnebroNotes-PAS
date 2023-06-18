package com.example.enebronotes.ui.screens.sign_in

import com.example.enebronotes.util.Result
import com.google.firebase.auth.FirebaseUser

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val signInStatus: SignInStatus = SignInStatus.Default
)


enum class SignInStatus {
    Default,
    Success,
    Failed,
    Loading,
}

fun Result<FirebaseUser>.toSignInStatus(): SignInStatus = when (this) {
    is Result.Success -> SignInStatus.Success
    is Result.Failure -> when (this.exception) {
        else -> SignInStatus.Failed
    }

    else -> SignInStatus.Loading
}
