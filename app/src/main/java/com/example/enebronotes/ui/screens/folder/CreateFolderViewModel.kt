package com.example.enebronotes.ui.screens.folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.enebronotes.data.folder.Folder
import com.example.enebronotes.data.folder.FolderRepository
import com.example.enebronotes.ui.common.components.ValidColors


/**
 * View Model to validate and insert items in the Room database.
 */
class CreateFolderViewModel(private val folderRepository: FolderRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
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
            folderRepository.insertFolder(folderUiState.folderDetails.toFolder())
        }
    }

    private fun validateInput(uiState: FolderDetails = folderUiState.folderDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && color.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class FolderUiState(
    val folderDetails: FolderDetails = FolderDetails(),
    val isEntryValid: Boolean = false
)

data class FolderDetails(
    val id: Long = 0,
    val title: String = "",
    val color: String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun FolderDetails.toFolder(): Folder = Folder(
    id = id,
    title = title,
    color = color.toLongOrNull() ?: 0
)

fun Folder.toFolderUiState(isEntryValid: Boolean = false): FolderUiState = FolderUiState(
    folderDetails = this.toFolderDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Folder.toFolderDetails(): FolderDetails = FolderDetails(
    id = id,
    title = title,
    color = color.toString()
)
