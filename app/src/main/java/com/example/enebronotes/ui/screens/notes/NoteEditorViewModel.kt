package com.example.enebronotes.ui.screens.notes

import android.content.res.Resources
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enebronotes.R
import com.example.enebronotes.data.cell.Cell
import com.example.enebronotes.data.cell.CellRepository
import com.example.enebronotes.data.note.NotesRepository
import com.example.enebronotes.data.storage.StorageRepository
import com.example.enebronotes.data.tenor_api.TenorApiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val TAG = "NoteEditorViewModel"

class NoteEditorViewModel(
    savedStateHandle: SavedStateHandle,
    val noteRepository: NotesRepository,
    val cellRepository: CellRepository,
    val storageRepository: StorageRepository,
    val tenorApiRepository: TenorApiRepository,
    val resources: Resources
) : ViewModel() {
    private val noteId: Long = checkNotNull(savedStateHandle[NoteEditorDestination.noteIdArg])
    private val noteTitle = MutableStateFlow("")
    private val cells = MutableStateFlow<List<Cell>>(listOf())
    private val searchText = MutableStateFlow("")
    private val gifUrls = MutableStateFlow<List<String>>(listOf())
    private val cellToInsertGif = MutableStateFlow<Cell?>(null)
    private val showBottomSheet = MutableStateFlow(false)

    private var noteTitleJob: Job? = null
    private var insertCellJob: Job? = null
    private var cellJobsHashMap: HashMap<Long, Job> = hashMapOf()
    private var apiJob: Job? = null


    init {
        viewModelScope.launch {
            noteTitle.value = noteRepository.getNoteStream(noteId).first()!!.title
            cells.value = cellRepository.getAllCellsByNoteStream(noteId).first().toMutableList()
        }
    }


    val uiState = combine(
        cells,
        noteTitle,
        searchText,
        gifUrls,
        cellToInsertGif
    ) { noteCells, _noteTitle, _searchText, _gifUrls, _cellToInsertGif ->
        NoteEditorUiState(
            noteTitle = _noteTitle,
            cells = noteCells,
            searchText = _searchText,
            gifUrls = _gifUrls,
            cellToInsertGif = _cellToInsertGif
        )
    }.combine(showBottomSheet) { noteEditorUiState, _showBottomSheet ->
        noteEditorUiState.copy(showBottomSheet = _showBottomSheet)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = NoteEditorUiState()
    )

    fun onNoteTitleChange(newNoteTitle: String) {
        noteTitle.value = newNoteTitle

        noteTitleJob?.cancel()

        noteTitleJob = viewModelScope.launch {
            val note = noteRepository.getNoteStream(noteId).first()
            val updatedNote = note!!.copy(title = newNoteTitle)
            noteRepository.updateNote(updatedNote)
            noteTitleJob = null
        }
    }

    fun onCellTitleChange(newCellTitle: String, oldCell: Cell) {
        val newCell = oldCell.copy(title = newCellTitle)

        updateCell(newCell, oldCell)
    }

    fun onCellContentChange(newCellContent: String, oldCell: Cell) {
        val newCell = oldCell.copy(content = newCellContent)

        updateCell(newCell, oldCell)
    }

    fun onMediaUriChange(uri: Uri, oldCell: Cell) {
        viewModelScope.launch {
            storageRepository.putFile(uri).collect {
                val newCell = oldCell.copy(mediaUri = it)
                updateCell(newCell, oldCell)
            }
        }

    }

    private fun updateCell(newCell: Cell, oldCell: Cell) {
        cells.update {
            cells.value.toMutableList().apply {
                val cellIndex = this.indexOfFirst {
                    it.id == oldCell.id
                }
                this[cellIndex] = newCell
            }
        }

        cellJobsHashMap[oldCell.id]?.cancel()

        val cellJob = viewModelScope.launch {
            cellRepository.updateCell(newCell)
            cellJobsHashMap.remove(oldCell.id)
        }

        cellJobsHashMap[oldCell.id] = cellJob
    }


    fun createNewCell() {
        if (insertCellJob == null || insertCellJob!!.isCompleted) {
            insertCellJob = viewModelScope.launch {
                val newCell = Cell(
                    title = "",
                    content = "",
                    mediaUri = null,
                    noteId = noteId
                )

                val newCellId = cellRepository.insertCell(newCell)

                cells.update {
                    cells.value.toMutableList().apply {
                        this.add(newCell.copy(id = newCellId))
                    }
                }
            }
        }
    }

    fun onCellRemoveClick(oldCell: Cell) {
        cells.value = cells.value.filter {
            it.id != oldCell.id
        }

        viewModelScope.launch {
            cellRepository.deleteCell(oldCell)
        }
    }

    fun onSearchClick() {
        if (apiJob == null || apiJob!!.isCompleted) {
            apiJob = viewModelScope.launch {
                val response = tenorApiRepository.getSearch(
                    query = uiState.value.searchText.trim(),
                    apiKey = resources.getString(R.string.google_api_key),
                    clientKey = resources.getString(R.string.google_app_id),
                )
                gifUrls.value = response.results.map { it.toUrl() }
                delay(1000)
                apiJob = null
            }
        }
    }

    fun onSearchTextChange(newString: String) {
        searchText.value = newString
    }

    fun onCellGifClick(cell: Cell) {
        cellToInsertGif.value = cell
        showBottomSheet.value = true

        if (apiJob == null || apiJob!!.isCompleted) {
            apiJob = viewModelScope.launch {
                val response = tenorApiRepository.getFeatured(
                    apiKey = resources.getString(R.string.google_api_key),
                    clientKey = resources.getString(R.string.google_app_id),
                )
                gifUrls.value = response.results.map { it.toUrl() }
                delay(1000)
                apiJob = null
            }
        }
    }

    fun onDismissBottomSheetRequest() {
        apiJob?.cancel()
        showBottomSheet.value = false
        cellToInsertGif.value = null
        gifUrls.value = listOf()
        searchText.value = ""
    }

    fun onGifClicked(gifUrl: String) {
        val newCell = cellToInsertGif.value?.copy(mediaUri = gifUrl)

        if (newCell != null) {
            updateCell(newCell, cellToInsertGif.value!!)
            onDismissBottomSheetRequest()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}