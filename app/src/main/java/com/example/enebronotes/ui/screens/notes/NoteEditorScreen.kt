package com.example.enebronotes.ui.screens.notes

import android.net.Uri
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enebronotes.R
import com.example.enebronotes.data.cell.Cell
import com.example.enebronotes.ui.AppViewModelProvider
import com.example.enebronotes.ui.common.components.CellComponent
import com.example.enebronotes.ui.common.components.TenorBottomSheet
import com.example.enebronotes.ui.screens.navigation.NavigationDestination
import com.example.enebronotes.ui.theme.EnebroNotesTheme

object NoteEditorDestination : NavigationDestination {
    override val route = "note_editor"
    override val titleRes = R.string.note_editor_screen_title
    const val noteIdArg = "noteId"
    val routeWithArgs = "$route/{$noteIdArg}"
}

@Composable
fun NoteEditorScreen(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteEditorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            NoteEditorAppBar(
                title = uiState.noteTitle,
                onTitleChange = viewModel::onNoteTitleChange,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButtonNewCell(onClick = viewModel::createNewCell)
        }
    ) { innerPadding ->
        NoteEditorBody(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 8.dp)
                .fillMaxSize(),
            cells = uiState.cells,
            onCellTitleChange = viewModel::onCellTitleChange,
            onCellContentChange = viewModel::onCellContentChange,
            onCellRemoveClick = viewModel::onCellRemoveClick,
            onMediaUriChange = viewModel::onMediaUriChange,
            onSearchTextChange = viewModel::onSearchTextChange,
            onCellGifClick = viewModel::onCellGifClick,
            onDismissBottomSheetRequest = viewModel::onDismissBottomSheetRequest,
            onGifClicked = viewModel::onGifClicked,
            showBottomSheet = uiState.showBottomSheet,
            searchText = uiState.searchText,
            onSearchClick = viewModel::onSearchClick,
            gifsUrls = uiState.gifUrls
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteEditorBody(
    cells: List<Cell>,
    onMediaUriChange: (Uri, Cell) -> Unit,
    onCellTitleChange: (String, Cell) -> Unit,
    onCellContentChange: (String, Cell) -> Unit,
    onCellRemoveClick: (Cell) -> Unit,
    onCellGifClick: (Cell) -> Unit,
    onGifClicked: (String) -> Unit,
    showBottomSheet: Boolean,
    onDismissBottomSheetRequest: () -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    gifsUrls: List<String>,
    modifier: Modifier = Modifier
) {
    if (showBottomSheet) {
        TenorBottomSheet(
            modifier = Modifier,
            onDismissRequest = onDismissBottomSheetRequest,
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onSearchClick = onSearchClick,
            gifsUrls = gifsUrls,
            onGifClicked = onGifClicked
        )
    }

    Surface(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(cells, key = { _, cell -> cell.id }) { index, cell ->
                CellComponent(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .animateItemPlacement(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ),
                    cell = cell,
                    currentPosition = index + 1,
                    numberOfCells = cells.size,
                    onMediaUriChange = { uri -> onMediaUriChange(uri, cell) },
                    onCellTitleChange = { onCellTitleChange(it, cell) },
                    onCellContentChange = { onCellContentChange(it, cell) },
                    onCellRemoveClick = { onCellRemoveClick(cell) },
                    onCellGifClick = { onCellGifClick(cell) }
                )
            }
        }
    }
}

@Composable
fun NoteEditorAppBar(
    title: String,
    onTitleChange: (String) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            val colors = if (title.isBlank())
                OutlinedTextFieldDefaults.colors()
            else
                OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                )
            OutlinedTextField(
                singleLine = true,
                colors = colors,
                value = title,
                onValueChange = onTitleChange
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun FloatingActionButtonNewCell(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LargeFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            Icons.Filled.Add,
            modifier = Modifier.size(48.dp),
            contentDescription =
            stringResource(R.string.create_new_cell_desc)
        )
    }
}

@Preview
@Composable
fun NoteEditorAppBarPreview() {
    EnebroNotesTheme {
        NoteEditorAppBar(
            title = "Title",
            onTitleChange = {},
            canNavigateBack = true,
            navigateUp = { }
        )
    }
}

@Preview
@Composable
fun NoteEditorFABNewCellPreview() {
    EnebroNotesTheme {
        FloatingActionButtonNewCell(onClick = {})
    }
}

@Preview
@Composable
fun NoteEditorBodyPreview() {
    val cells = listOf(
        Cell(
            0, "Cell 1",
            "Cell 1 preview content", null, 0
        ),
        Cell(
            1, "Cell 2",
            "Cell 2 preview content", null, 0
        )
    )
    EnebroNotesTheme {
        NoteEditorBody(
            modifier = Modifier
                .fillMaxSize(),
            cells = cells,
            onCellTitleChange = { _, _ -> },
            onCellContentChange = { _, _ -> },
            onCellRemoveClick = {},
            onMediaUriChange = { _, _ -> },
            gifsUrls = listOf(),
            onSearchClick = {},
            searchText = "",
            onSearchTextChange = {},
            onGifClicked = {},
            onDismissBottomSheetRequest = {},
            onCellGifClick = {},
            showBottomSheet = false
        )
    }
}

@Preview
@Composable
fun NoteEditorBodyWithScaffoldPreview() {
    val cells = listOf(
        Cell(
            0, "Cell 1",
            "Cell 1 preview content", null, 0
        ),
        Cell(
            1, "Cell 2",
            "Cell 2 preview content", null, 0
        )
    )
    EnebroNotesTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NoteEditorBody(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                cells = cells,
                onCellTitleChange = { _, _ -> },
                onCellContentChange = { _, _ -> },
                onCellRemoveClick = {},
                onMediaUriChange = { _, _ -> },
                gifsUrls = listOf(),
                onSearchClick = {},
                searchText = "",
                onSearchTextChange = {},
                onGifClicked = {},
                onDismissBottomSheetRequest = {},
                onCellGifClick = {},
                showBottomSheet = false
            )
        }
    }
}
