package com.example.enebronotes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.enebronotes.ui.screens.navigation.EnebroNotesNavHost

@Composable
fun EnebroNotesApp(navController: NavHostController = rememberNavController()) {
    EnebroNotesNavHost(navController = navController)
}