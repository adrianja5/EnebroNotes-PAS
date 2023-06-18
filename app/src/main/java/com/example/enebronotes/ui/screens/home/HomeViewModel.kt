package com.example.enebronotes.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enebronotes.data.auth.AuthRepository
import com.example.enebronotes.data.folder.FolderRepository
import com.example.enebronotes.data.note.Note
import com.example.enebronotes.data.note.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    val folderRepository: FolderRepository,
    val noteRepository: NotesRepository,
    val authRepository: AuthRepository,
) : ViewModel() {
    private val showFloatingActionButtonOptions = MutableStateFlow(false)
    private val listOfCheckedDirectories = MutableStateFlow<List<Long>>(listOf())
    private val showCreateNoteDialog = MutableStateFlow(false)
    val selectedFolder = mutableStateOf(-1L)


    val uiState = combine(
        folderRepository.getAllFoldersStream(),
        noteRepository.getAllNotesStream(),
        authRepository.getCurrentUserFlow(),
        showFloatingActionButtonOptions,
        listOfCheckedDirectories
    ) { folders, notes, _currentUser, _showFloatingActionButtonOptions, _listOfCheckedDirectories ->
        HomeUiState(
            folders = folders,
            notes = notes,
            userIsLoggedIn = _currentUser != null,
            showFloatingActionButtonOptions = _showFloatingActionButtonOptions,
            listOfCheckedDirectories = _listOfCheckedDirectories
        )
    }.combine(showCreateNoteDialog) { homeUiState, _showCreateNoteDialog ->
        homeUiState.copy(
            showCreateNoteDialog = _showCreateNoteDialog
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = HomeUiState()
    )

    fun removeDirectories(folderIds: List<Long>) {
        folderIds.forEach {
            val folderFlow = folderRepository.getFolderStream(it)
            viewModelScope.launch {
                folderFlow.collect { folder ->
                    if (folder != null) {
                        folderRepository.deleteFolder(folder)
                    }
                }

            }

        }
    }

    fun onMainFloatingActionButtonClick() {
        showFloatingActionButtonOptions.value = !showFloatingActionButtonOptions.value
    }

    fun updateListOfCheckedDirectories(folderId: Long, selected: Boolean) {
        if (selected) {
            listOfCheckedDirectories.value += listOf(folderId)
        } else {
            listOfCheckedDirectories.value = listOfCheckedDirectories.value.filter {
                it != folderId
            }
        }

    }

    fun selectFolder(folderId: Long) {
        selectedFolder.value = folderId
    }

    fun deleteNote(note: Note) {

        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }

    }

    fun clearListOfCheckedDirectories() {
        listOfCheckedDirectories.value = listOf()
    }

    fun singOut() {
        authRepository.logout()
    }

    fun onCreateNewNote() {
        showCreateNoteDialog.value = true
    }

    fun onDismissRequest() {
        showCreateNoteDialog.value = false
    }

    suspend fun onConfirmRequest(folderId: Long): Long {
        val newNote = Note(
            title = "",
            folderId = folderId
        )
        val newNoteId = noteRepository.insertNote(newNote)
        showCreateNoteDialog.value = false
        return newNoteId
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
