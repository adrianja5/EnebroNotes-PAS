package com.example.enebronotes.ui.screens.sign_up

import com.example.enebronotes.util.Result
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser


data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val repeatPasswordIncorrect: Boolean = false,
    val passwordVisible: Boolean = false,
    val signUpStatus: SignUpStatus = SignUpStatus.Default
)

enum class SignUpStatus {
    Default,
    Success,
    Failed,
    PassWeak,
    Loading,
}

fun Result<FirebaseUser>.toSignUpStatus(): SignUpStatus = when (this) {
    is Result.Success -> SignUpStatus.Success
    is Result.Failure -> when (this.exception) {
        is FirebaseAuthWeakPasswordException -> SignUpStatus.PassWeak
        else -> SignUpStatus.Failed
    }

    else -> SignUpStatus.Loading
}
