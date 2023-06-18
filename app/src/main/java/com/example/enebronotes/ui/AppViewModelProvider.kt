package com.example.enebronotes.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.enebronotes.EnebroNotesApplication
import com.example.enebronotes.ui.screens.folder.CreateFolderViewModel
import com.example.enebronotes.ui.screens.folder.UpdateFolderViewModel
import com.example.enebronotes.ui.screens.home.HomeViewModel
import com.example.enebronotes.ui.screens.notes.NoteEditorViewModel
import com.example.enebronotes.ui.screens.sign_in.SignInViewModel
import com.example.enebronotes.ui.screens.sign_up.SignUpViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for SignUpViewModel
        initializer {
            SignUpViewModel(
                this.createSavedStateHandle(),
                repository = enebroNotesApplication().container.authRepository,
                resources = enebroNotesApplication().container.resources
            )
        }
        // Initializer for SignInViewModel
        initializer {
            SignInViewModel(
                repository = enebroNotesApplication().container.authRepository,
                resources = enebroNotesApplication().container.resources
            )
        }
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                folderRepository = enebroNotesApplication().container.folderRepository,
                noteRepository = enebroNotesApplication().container.noteRepository,
                authRepository = enebroNotesApplication().container.authRepository,
            )
        }
        // Initializer for CreateFolderViewModel
        initializer {
            CreateFolderViewModel(
                folderRepository = enebroNotesApplication().container.folderRepository,
            )
        }
        // Initializer for NoteEditorViewModel
        initializer {
            NoteEditorViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                storageRepository = enebroNotesApplication().container.storageRepository,
                noteRepository = enebroNotesApplication().container.noteRepository,
                cellRepository = enebroNotesApplication().container.cellRepository,
                resources = enebroNotesApplication().container.resources,
                tenorApiRepository = enebroNotesApplication().container.tenorApiRepository
            )
        }
        // Initializer for UpdateFolderViewModel
        initializer {
            UpdateFolderViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                folderRepository = enebroNotesApplication().container.folderRepository,
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [EnebroNotesApplication].
 */
fun CreationExtras.enebroNotesApplication(): EnebroNotesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EnebroNotesApplication)