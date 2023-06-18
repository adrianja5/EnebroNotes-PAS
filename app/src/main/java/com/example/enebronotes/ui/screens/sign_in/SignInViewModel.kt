package com.example.enebronotes.ui.screens.sign_in

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enebronotes.data.auth.AuthRepository
import com.example.enebronotes.util.asResult
import kotlinx.coroutines.launch

private const val TAG = "SignInVM"

class SignInViewModel(
    private val repository: AuthRepository,
    val resources: Resources
) : ViewModel() {
    var uiState = mutableStateOf(SignInUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onVisibilityIconClick() {
        uiState.value = uiState.value.copy(passwordVisible = !uiState.value.passwordVisible)
    }

    fun onSignInClick() {
        viewModelScope.launch {
            repository.signIn(email, password)
                .asResult()
                .collect { userResource ->
                    val status = userResource.toSignInStatus()
                    uiState.value = uiState.value.copy(signInStatus = status)
                }
            Log.e(com.example.enebronotes.ui.screens.sign_up.TAG, "HE TERMINADO EL SIGN IN")
        }
    }
}