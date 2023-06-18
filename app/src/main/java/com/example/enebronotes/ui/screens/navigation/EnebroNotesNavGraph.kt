package com.example.enebronotes.ui.screens.navigation

import CreateFolderDestination
import CreateFolderScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.enebronotes.ui.screens.folder.FolderUpdateDestination
import com.example.enebronotes.ui.screens.folder.UpdateFolderScreen
import com.example.enebronotes.ui.screens.home.HomeDestination
import com.example.enebronotes.ui.screens.home.HomeScreen
import com.example.enebronotes.ui.screens.notes.NoteEditorDestination
import com.example.enebronotes.ui.screens.notes.NoteEditorScreen
import com.example.enebronotes.ui.screens.sign_in.SignInDestination
import com.example.enebronotes.ui.screens.sign_in.SignInScreen
import com.example.enebronotes.ui.screens.sign_up.SignUpDestination
import com.example.enebronotes.ui.screens.sign_up.SignUpScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun EnebroNotesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                canNavigateBack = navController.canNavigateBack(),
                navigateToNewFolderEditor = {
                    navController.navigate(
                        CreateFolderDestination.route
                    )
                },
                navigateToUpdateFolderEditor = { folderId ->
                    navController.navigate(
                        "${FolderUpdateDestination.route}/$folderId"
                    )
                },
                navigateToNewNoteEditor = { newNoteId ->
                    navController.navigate(
                        "${NoteEditorDestination.route}/$newNoteId"
                    )
                },
                navigateToSignIn = {
                    navController.navigateAndClear(SignInDestination.route)
                }
            )
        }
        composable(route = CreateFolderDestination.route) {
            CreateFolderScreen(
                navigateBack = navController::navigateUp,
                onNavigateUp = navController::navigateUp,
                canNavigateBack = navController.canNavigateBack()
            )
        }
        composable(
            route = SignUpDestination.routeWithArgs,
            arguments = listOf(navArgument(SignUpDestination.initialEmailArg) {
                nullable = true
                type = NavType.StringType
            })
        ) {
            SignUpScreen(
                canNavigateBack = navController.canNavigateBack(),
                onSignUpSuccess = {
                    navController.navigateAndClear(HomeDestination.route)
                },
                onNavigateUp = navController::navigateUp
            )
        }

        composable(route = SignInDestination.route) {
            SignInScreen(
                canNavigateBack = navController.canNavigateBack(),
                onSignInSuccess = {
                    navController.navigateAndClear(HomeDestination.route)
                },
                onCreateAccountClicked = { initialString ->
                    navController.navigate(
                        "${SignUpDestination.route}?${SignUpDestination.initialEmailArg}=$initialString"
                    )
                }
            )
        }

        composable(
            route = NoteEditorDestination.routeWithArgs,
            arguments = listOf(navArgument(NoteEditorDestination.noteIdArg) {
                type = NavType.LongType
            })
        ) {
            NoteEditorScreen(
                canNavigateBack = navController.canNavigateBack(),
                navigateUp = navController::navigateUp
            )
        }

        composable(
            route = FolderUpdateDestination.routeWithArgs,
            arguments = listOf(navArgument(FolderUpdateDestination.folderIdArg) {
                type = NavType.IntType
            })
        ) {
            UpdateFolderScreen(
                canNavigateBack = navController.canNavigateBack(),
                navigateUp = navController::navigateUp
            )
        }

    }
}

// https://stackoverflow.com/a/71615576
fun NavHostController.navigateWithPopUp(
    toRoute: String,  // route name where you want to navigate
    fromRoute: String // route you want from popUpTo.
) {
    this.navigate(toRoute) {
        popUpTo(fromRoute) {
            inclusive = true // It can be changed to false if you
            // want to keep your fromRoute exclusive
        }
    }
}

fun NavHostController.navigateAndClear(
    toRoute: String,  // route name where you want to navigate
) {
    this.navigate(toRoute) {
        popUpTo(0)
    }
}

fun NavHostController.canNavigateBack(): Boolean = this.previousBackStackEntry != null