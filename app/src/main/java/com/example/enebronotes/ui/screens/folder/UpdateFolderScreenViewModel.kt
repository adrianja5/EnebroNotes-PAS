package com.example.enebronotes.ui.screens.folder


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enebronotes.data.folder.FolderRepository
import com.example.enebronotes.ui.common.components.ValidColors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * View Model to validate and insert items in the Room database.
 */
class UpdateFolderViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    private val folderId: Long = checkNotNull(savedStateHandle[FolderUpdateDestination.folderIdArg])

    init {
        viewModelScope.launch {
            val folderDetails =
                folderRepository.getFolderStream(folderId).first()!!.toFolderDetails()
            selectedColor =
                ValidColors.values().first { it.color.toString() == folderDetails.color }
            folderUiState = folderUiState.copy(
                folderDetails = folderDetails,
                isEntryValid = validateInput(folderDetails)
            )
        }
    }

    var folderUiState by mutableStateOf(FolderUiState())
        private set

    var selectedColor: ValidColors? by mutableStateOf(null)
        private set

    fun setColor(color: String) {
        var colorEnum = ValidColors.values().filter { it.name == color }
        selectedColor = colorEnum.first()
        var folderDetails = FolderDetails(
            color = selectedColor?.color.toString(),
            id = folderUiState.folderDetails.id,
            title = folderUiState.folderDetails.title
        )
        folderUiState =
            FolderUiState(
                folderDetails = folderDetails,
                isEntryValid = validateInput(folderDetails)
            )
    }


    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(folderDetails: FolderDetails) {
        folderUiState =
            FolderUiState(
                folderDetails = folderDetails,
                isEntryValid = validateInput(folderDetails)
            )
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            folderRepository.updateFolder(folderUiState.folderDetails.toFolder())
        }
    }

    private fun validateInput(uiState: FolderDetails = folderUiState.folderDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && color.isNotBlank()
        }
    }
}


