package com.example.enebronotes.ui.common.components

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.enebronotes.R
import com.example.enebronotes.data.cell.Cell
import com.example.enebronotes.ui.theme.EnebroNotesTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

const val TAG = "CellComponent"

@Composable
fun CellComponent(
    cell: Cell,
    currentPosition: Int,
    numberOfCells: Int,
    onMediaUriChange: (Uri) -> Unit,
    onCellTitleChange: (String) -> Unit,
    onCellContentChange: (String) -> Unit,
    onCellRemoveClick: () -> Unit,
    onCellGifClick: () -> Unit,
    modifier: Modifier = Modifier,
    currentEditor: String? = null,
    textFieldBlocked: Boolean = false,
) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uriCamera = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.example.enebronotes.provider", file
    )


    val launcherGallery = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            uri.lastPathSegment?.let { onMediaUriChange(uri) }
        }

    }

    val launcherCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { it ->
            if (uriCamera != null) {
                uriCamera.lastPathSegment?.let { onMediaUriChange(uriCamera) }
            }
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            launcherCamera.launch(uriCamera)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                //verticalAlignment = Alignment.CenterVertically
            ) {
                val titleColors = if (cell.title.isBlank())
                    OutlinedTextFieldDefaults.colors()
                else
                    OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                    )
                TextField(
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                    singleLine = true,
                    colors = titleColors,
                    value = cell.title,
                    onValueChange = onCellTitleChange
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "$currentPosition / $numberOfCells",
                    fontSize = 18.sp
                )
            }

            if (cell.mediaUri != null) {
                ShowMedia(cell.mediaUri)
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                enabled = !textFieldBlocked,
                label = if (currentEditor != null) {
                    { Text(stringResource(R.string.someone_is_writing, currentEditor)) }
                } else {
                    null
                },
                value = cell.content,
                onValueChange = onCellContentChange
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row() {
                    IconButton(onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, "android.permission.CAMERA")
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            launcherCamera.launch(uriCamera)
                        } else {
                            // Request a permission
                            permissionLauncher.launch("android.permission.CAMERA")
                        }
                    }) {
                        Icon(
                            Icons.Rounded.AddAPhoto,
                            contentDescription = stringResource(R.string.take_photo_desc)
                        )
                    }
                    IconButton(onClick = { launcherGallery.launch("image/*") }) {
                        Icon(
                            Icons.Rounded.AddPhotoAlternate,
                            contentDescription = stringResource(R.string.get_photo_gallery_dec)
                        )
                    }
                    IconButton(onClick = onCellGifClick) {
                        Icon(
                            Icons.Filled.Gif,
                            contentDescription = stringResource(R.string.get_gif_tenor_dec)
                        )
                    }
                }
                IconButton(
                    onClick = onCellRemoveClick,
                    enabled = !textFieldBlocked
                ) {
                    Icon(
                        Icons.Rounded.DeleteForever,
                        contentDescription = stringResource(R.string.delete_cell_dec)
                    )
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}

@Preview
@Composable
fun CellComponentWithoutCurrentEditorPreview() {
    val cell = Cell(
        title = "Cell1",
        content = "Preview text",
        mediaUri = null,
        noteId = 0
    )
    EnebroNotesTheme {
        CellComponent(
            cell = cell,
            currentPosition = 5,
            numberOfCells = 10,
            textFieldBlocked = false,
            onCellTitleChange = {},
            onCellContentChange = {},
            onCellRemoveClick = {},
            onMediaUriChange = { _ -> },
            onCellGifClick = {}
        )
    }
}

@Preview
@Composable
fun CellComponentWithCurrentEditorPreview() {
    val cell = Cell(
        title = "Cell2",
        content = "Preview text",
        mediaUri = null,
        noteId = 0
    )
    EnebroNotesTheme {
        CellComponent(
            cell = cell,
            currentPosition = 5,
            numberOfCells = 10,
            textFieldBlocked = true,
            currentEditor = "Pepe",
            onCellTitleChange = {},
            onCellContentChange = {},
            onCellRemoveClick = {},
            onMediaUriChange = { _ -> },
            onCellGifClick = {}
        )
    }
}