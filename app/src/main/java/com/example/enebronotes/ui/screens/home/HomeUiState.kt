package com.example.enebronotes.ui.screens.home

import com.example.enebronotes.data.folder.Folder
import com.example.enebronotes.data.note.Note

data class HomeUiState(
    val folders: List<Folder> = listOf(),
    val notes: List<Note> = listOf(),
    val showFloatingActionButtonOptions: Boolean = false,
    val userIsLoggedIn: Boolean = true,
    val listOfCheckedDirectories: List<Long> = listOf(),
    val showCreateNoteDialog: Boolean = false
)
