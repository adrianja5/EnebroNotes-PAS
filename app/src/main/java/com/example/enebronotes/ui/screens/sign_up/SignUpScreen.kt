package com.example.enebronotes.ui.screens.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enebronotes.R
import com.example.enebronotes.ui.AppViewModelProvider
import com.example.enebronotes.ui.common.components.PasswordTextField
import com.example.enebronotes.ui.screens.navigation.NavigationDestination


object SignUpDestination : NavigationDestination {
    override val route = "sign_up"
    override val titleRes = R.string.sign_up_screen_title
    const val initialEmailArg = "initialEmail"
    val routeWithArgs = "$route?$initialEmailArg={$initialEmailArg}"
}

@Composable
fun SignUpAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}


@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.signUpStatus) {
        when (uiState.signUpStatus) {
            SignUpStatus.Success -> onSignUpSuccess()
            SignUpStatus.PassWeak ->
                snackbarHostState.showSnackbar(
                    viewModel.resources.getString(R.string.sign_up_weak_password_message)
                )

            SignUpStatus.Failed ->
                snackbarHostState.showSnackbar(
                    viewModel.resources.getString(R.string.unknown_fail_message)
                )

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SignUpAppBar(
                currentScreen = stringResource(SignUpDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                modifier = Modifier
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("SIGN UP", fontSize = 24.sp)
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChange,
                        label = {
                            Text(text = "Name")
                        },
                        keyboardOptions = if (uiState.name.isNotBlank()) {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            )
                        } else {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.None
                            )
                        }
                    )
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        label = {
                            Text(text = "Email")
                        },
                        keyboardOptions = if (uiState.email.isNotBlank()) {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            )
                        } else {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.None
                            )
                        }
                    )
                    PasswordTextField(
                        password = uiState.password,
                        onPasswordChange = viewModel::onPasswordChange,
                        passwordVisible = uiState.passwordVisible,
                        onVisibilityIconClick = viewModel::onVisibilityIconClick,
                        keyboardOptions = if (uiState.email.isNotBlank()
                            && uiState.password.isNotBlank()
                        ) {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            )
                        } else {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.None
                            )
                        }
                    )
                    PasswordTextField(
                        password = uiState.repeatPassword,
                        onPasswordChange = viewModel::onRepeatPasswordChange,
                        isError = uiState.repeatPasswordIncorrect,
                        passwordVisible = uiState.passwordVisible,
                        onVisibilityIconClick = viewModel::onVisibilityIconClick,
                        keyboardOptions = if (uiState.name.isNotBlank() && uiState.email.isNotBlank()
                            && uiState.password.isNotBlank() && uiState.repeatPassword.isNotBlank()
                        ) {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            )
                        } else {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.None
                            )
                        },
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.onSignUpClick() }
                        )
                    )
                    Button(
                        enabled = uiState.name.isNotBlank() && uiState.email.isNotBlank() && uiState.password.isNotBlank() && uiState.repeatPassword.isNotBlank(),
                        onClick = viewModel::onSignUpClick
                    ) {
                        Text(text = "Create account")
                    }
                    if (uiState.signUpStatus == SignUpStatus.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}
