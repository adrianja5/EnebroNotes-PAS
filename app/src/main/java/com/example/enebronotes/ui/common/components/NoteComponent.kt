package com.example.enebronotes.ui.common.components


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enebronotes.R
import com.example.enebronotes.data.note.Note

@Composable
fun NoteComponent(
    note: Note,
    editNote: (Long) -> Unit,
    deleteNote: (Note) -> Unit
) {

    Card(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    editNote(note.id)
                }
            )
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Text(note.title.ifEmpty { stringResource(R.string.untitled_note) })
            }


            Column() {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()  //important
                        .width(2.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            Icons.Rounded.Share,
                            contentDescription = stringResource(R.string.share_note),
                        )
                    }
                    IconButton(
                        onClick = { deleteNote(note) },
                    ) {
                        Icon(
                            Icons.Rounded.DeleteForever,
                            contentDescription = stringResource(R.string.delete_cell_dec),
                        )
                    }
                }

            }

        }
    }


}

@Preview
@Composable
fun NoteComponentPreview(
) {
    NoteComponent(Note(
        id = 10,
        title = "Note Title",
        folderId = 10
    ),
        editNote = {},
        deleteNote = {}
    )

}