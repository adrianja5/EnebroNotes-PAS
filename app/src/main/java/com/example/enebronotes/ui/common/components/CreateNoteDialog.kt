package com.example.enebronotes.ui.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enebronotes.R
import com.example.enebronotes.data.folder.Folder
import com.example.enebronotes.ui.theme.EnebroNotesTheme

@Composable
fun CreateNoteDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Long) -> Unit,
    folders: List<Folder>,
    modifier: Modifier = Modifier
) {
    var selectedFolder: Folder? by rememberSaveable { mutableStateOf(null) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.select_folder))
        }, text = {
            Divider()
            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
                    .heightIn(0.dp, 232.dp)
            ) {
                items(folders) { folder ->
                    FolderSelectionRow(
                        folder = folder,
                        selected = (selectedFolder != null && folder.id == selectedFolder!!.id),
                        onSelect = { selectedFolder = folder }
                    )
                }
            }
            Divider()
        }, confirmButton = {
            TextButton(
                onClick = {
                    val folderId = selectedFolder!!.id
                    selectedFolder = null
                    onConfirmRequest(folderId)
                },
                enabled = selectedFolder != null
            ) {
                Text(stringResource(R.string.create_note))
            }
        }, dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}


@Composable
fun FolderSelectionRow(
    folder: Folder,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton
            )
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)

        Spacer(modifier = Modifier.width(32.dp))

        Text(text = folder.title)
    }
}

@Preview
@Composable
fun FolderSelectionRowUnselectedPreview() {
    val folder = Folder(0, "A", 0)
    EnebroNotesTheme() {
        FolderSelectionRow(folder = folder, selected = false, onSelect = {})
    }
}

@Preview
@Composable
fun FolderSelectionRowSelectedPreview(
) {
    val folder = Folder(0, "A", 0)
    EnebroNotesTheme() {
        FolderSelectionRow(folder = folder, selected = true, onSelect = {})
    }
}

@Preview
@Composable
fun CreateNewDialogPreview() {
    val folders = listOf(
        Folder(0, "A", 0),
        Folder(1, "B", 0),
        Folder(2, "C", 0),
        Folder(3, "D", 0),
        Folder(4, "E", 0),
        Folder(5, "F", 0),
        Folder(6, "G", 0),
        Folder(7, "H", 0),
        Folder(8, "I", 0),
        Folder(9, "J", 0),
    )
    EnebroNotesTheme() {
        CreateNoteDialog(
            onDismissRequest = {},
            onConfirmRequest = {},
            folders = folders,
        )
    }
}

