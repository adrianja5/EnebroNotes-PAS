package com.example.enebronotes.ui.screens.sign_up

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enebronotes.data.auth.AuthRepository
import com.example.enebronotes.util.asResult
import kotlinx.coroutines.launch

const val TAG = "SIGN_UP"

class SignUpViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository,
    val resources: Resources
) : ViewModel() {
    private val initialEmail: String = savedStateHandle[SignUpDestination.initialEmailArg] ?: ""

    var uiState = mutableStateOf(SignUpUiState(email = initialEmail))
        private set

    private val name
        get() = uiState.value.name
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password


    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(
            repeatPassword = newValue,
            repeatPasswordIncorrect = false
        )
    }

    fun onVisibilityIconClick() {
        uiState.value = uiState.value.copy(passwordVisible = !uiState.value.passwordVisible)
    }

    fun onSignUpClick() {
        if (password != uiState.value.repeatPassword) {
            Log.d(TAG, "Password repetida distinta de password")
            uiState.value = uiState.value.copy(repeatPasswordIncorrect = true)
            return
        }

        viewModelScope.launch {
            repository.signup(name, email, password)
                .asResult()
                .collect { userResource ->
                    val status = userResource.toSignUpStatus()
                    uiState.value = uiState.value.copy(signUpStatus = status)
                }
            Log.e(TAG, "HE TERMINADO EL SIGN UP")
        }

    }
}
