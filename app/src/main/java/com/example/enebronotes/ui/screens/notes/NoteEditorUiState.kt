package com.example.enebronotes.ui.screens.notes

import com.example.enebronotes.data.cell.Cell

data class NoteEditorUiState(
    val noteTitle: String = "",
    val cells: List<Cell> = listOf(),
    val searchText: String = "",
    val gifUrls: List<String> = listOf(),
    val cellToInsertGif: Cell? = null,
    val showBottomSheet: Boolean = false,
)
