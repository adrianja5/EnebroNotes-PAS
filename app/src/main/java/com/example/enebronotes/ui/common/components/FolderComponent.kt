package com.example.enebronotes.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enebronotes.data.folder.Folder
import com.example.enebronotes.ui.common.shapes.FolderBackShape
import com.example.enebronotes.ui.common.shapes.FolderShape
import com.example.enebronotes.ui.theme.EnebroNotesTheme


enum class ValidColors(val color: Long) {
    WHITE(0xFFFFFFFF),
    RED(0xFFFF0000),
    BLUE(0xFF1510B0),
    GREEN(0xFF1FA510),
    GREEN2(0xFF1BDC1B),
    RED2(0xFFD04B19),
    BLUE2(0xFF3D49C2),


}

@Composable
fun FolderComponent(
    folder: Folder,
    onLongPress: () -> Unit,
    checkMode: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    updateDeleteFolder: (Long, Boolean) -> Unit,
    selectedFolder: MutableState<Long>,
    selectFolder: (Long) -> Unit,
) {


    OutlinedButton(
        border = BorderStroke(0.dp, Color(0)),
        modifier = modifier,
        onClick = { /*TODO*/ },


        ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
        ) {

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                FullFolderShapeColor(
                    color = Color(folder.color),
                    title = folder.title,
                    checkMode = checkMode,
                    onLongPress = onLongPress,
                    updateDeleteFolder = updateDeleteFolder,
                    folderId = folder.id,
                    selectedFolder = selectedFolder,
                    selectFolder = selectFolder
                )
            }


        }
    }


}


@Composable
fun FullFolderShapeColor(
    color: Color,
    title: String,
    checkMode: MutableState<Boolean>,
    onLongPress: () -> Unit,
    updateDeleteFolder: (Long, Boolean) -> Unit,
    folderId: Long,
    selectedFolder: MutableState<Long>,
    selectFolder: (Long) -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(false) }

    selected = selected && checkMode.value
    Box(modifier = Modifier
        .padding(top = 32.dp)
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    onLongPress()
                    selected = true
                    updateDeleteFolder(folderId, true)
                },
                onTap = {
                    if (checkMode.value) {
                        selected = !selected
                        updateDeleteFolder(folderId, selected)
                    } else {
                        if (folderId != selectedFolder.value) {
                            selectFolder(folderId)
                        } else {
                            selectFolder(-1)
                        }
                    }

                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .size(width = 128.dp, height = 102.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = FolderShape()
                ),
            shape = FolderShape(),
            color = if (selectedFolder.value == folderId) Color.Gray else Color.White,
        ) {
        }
        Surface(
            modifier = Modifier
                .size(width = 128.dp, height = 102.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = FolderBackShape()
                ),
            shape = FolderBackShape(),
            color = color,
        ) {
        }
        Text(text = title, Modifier.padding(top = 80.dp, start = 8.dp, bottom = 8.dp), Color.Black)
        if (checkMode.value && selected) {
            Icon(
                Icons.Rounded.Check,
                "Check"
            )
        }
    }
}

@Preview
@Composable
fun FolderPreview() {
    EnebroNotesTheme {
        var checkMode = remember { mutableStateOf(true) }
        var selectedFolder = remember { mutableStateOf(-1L) }
        FolderComponent(
            folder = Folder(0, "test", 0xFFFF0000),
            onLongPress = {},
            checkMode = checkMode,
            updateDeleteFolder = { _, _ -> },
            selectedFolder = selectedFolder,
            selectFolder = {}
        )

    }
}