package com.example.enebronotes.ui.screens.sign_in

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enebronotes.R
import com.example.enebronotes.ui.AppViewModelProvider
import com.example.enebronotes.ui.common.components.PasswordTextField
import com.example.enebronotes.ui.screens.navigation.NavigationDestination

private const val TAG: String = "SignInScreen"

object SignInDestination : NavigationDestination {
    override val route = "sign_in"
    override val titleRes = R.string.sign_in_screen_title
}

@Composable
fun SignInAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
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
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onCreateAccountClicked: (String) -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState

    Log.e(TAG, "Pasando por el Screen")

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.signInStatus) {
        Log.e(TAG, "Dentro del LaunchedEffect con estado ${uiState.signInStatus}")
        when (uiState.signInStatus) {
            SignInStatus.Success -> onSignInSuccess()
            SignInStatus.Failed ->
                snackbarHostState.showSnackbar(
                    viewModel.resources.getString(R.string.unknown_fail_message)
                )

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SignInAppBar(
                title = stringResource(id = SignInDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = {},
                modifier = Modifier
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("SIGN IN", fontSize = 24.sp)
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        singleLine = true,
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
                        keyboardOptions = if (uiState.email.isNotBlank() && uiState.password.isNotBlank()) {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            )
                        } else {
                            KeyboardOptions.Default.copy(
                                imeAction = ImeAction.None
                            )
                        },
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.onSignInClick() }
                        )
                    )

                    Button(
                        enabled = uiState.email.isNotBlank() && uiState.password.isNotBlank(),
                        onClick = viewModel::onSignInClick
                    ) {
                        Text(text = "Sign in")
                    }

                    ClickableText(
                        text = AnnotatedString("Create account")
                    ) { onCreateAccountClicked(uiState.email) }
                    if (uiState.signInStatus == SignInStatus.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}
