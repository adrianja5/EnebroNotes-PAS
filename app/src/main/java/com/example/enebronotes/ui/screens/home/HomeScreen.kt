package com.example.enebronotes.ui.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.rounded.CreateNewFolder
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enebronotes.R
import com.example.enebronotes.data.folder.Folder
import com.example.enebronotes.data.note.Note
import com.example.enebronotes.ui.AppViewModelProvider
import com.example.enebronotes.ui.common.components.CreateNoteDialog
import com.example.enebronotes.ui.common.components.FolderComponent
import com.example.enebronotes.ui.common.components.NoteComponent
import com.example.enebronotes.ui.common.components.OptionVerticalMore
import com.example.enebronotes.ui.common.components.VerticalMoreMenu
import com.example.enebronotes.ui.screens.navigation.NavigationDestination
import com.example.enebronotes.ui.screens.sign_up.TAG
import com.example.enebronotes.ui.theme.EnebroNotesTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    options: List<OptionVerticalMore>
) {

    var openVerticalMore by rememberSaveable { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {
                openVerticalMore = true
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
                if (openVerticalMore) {
                    VerticalMoreMenu(
                        options,
                        onClickItem = { openVerticalMore = false },
                        modifier = modifier
                    )
                }
            }
        }
    )

}

@Composable
fun HomeScreen(
    navigateToNewNoteEditor: (Long) -> Unit,
    navigateToNewFolderEditor: () -> Unit,
    navigateToUpdateFolderEditor: (Long) -> Unit,
    navigateToSignIn: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var checkMode = rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    val options = getOptions(viewModel, uiState, checkMode, navigateToUpdateFolderEditor)

    Log.e("canNavigateBack", canNavigateBack.toString())

    LaunchedEffect(uiState.userIsLoggedIn) {
        Log.e(TAG, uiState.userIsLoggedIn.toString())
        if (!uiState.userIsLoggedIn) {
            navigateToSignIn()
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            MultipleFloatingActionButtonFolderNoteCreation(
                showOptions = uiState.showFloatingActionButtonOptions,
                navigateToNewFolderEditor = navigateToNewFolderEditor,
                onMainFloatingActionButtonClick = viewModel::onMainFloatingActionButtonClick,
                onCreateNewNote = viewModel::onCreateNewNote
            )
        },
        topBar = {
            HomeAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = {},
                modifier = Modifier,
                options = options
            )
        }
    ) { innerPadding ->
        HomeScreenBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            folders = uiState.folders,
            notes = uiState.notes,
            updateListOfCheckedDirectories = viewModel::updateListOfCheckedDirectories,
            showCreateNoteDialog = uiState.showCreateNoteDialog,
            onDismissDialogRequest = viewModel::onDismissRequest,
            onConfirmDialogRequest = { idFolder ->
                coroutineScope.launch {
                    val newNoteId = viewModel.onConfirmRequest(idFolder)
                    navigateToNewNoteEditor(newNoteId)
                }

            },
            checkMode = checkMode,
            navigateToNewNoteEditor = navigateToNewNoteEditor,
            deleteNote = viewModel::deleteNote,
            selectedFolder = viewModel.selectedFolder,
            selectFolder = viewModel::selectFolder
        )
    }
}

@Composable
fun HomeScreenBody(
    folders: List<Folder>,
    notes: List<Note>,
    selectedFolder: MutableState<Long>,
    selectFolder: (Long) -> Unit,
    showCreateNoteDialog: Boolean,
    onDismissDialogRequest: () -> Unit,
    onConfirmDialogRequest: (Long) -> Unit,
    modifier: Modifier = Modifier,
    updateListOfCheckedDirectories: (Long, Boolean) -> Unit,
    navigateToNewNoteEditor: (Long) -> Unit,
    checkMode: MutableState<Boolean>,
    deleteNote: (Note) -> Unit
) {

    if (showCreateNoteDialog) {
        CreateNoteDialog(
            onDismissRequest = onDismissDialogRequest,
            onConfirmRequest = onConfirmDialogRequest,
            folders = folders
        )
    }
    Column(modifier = modifier) {
        LazyRow(
            modifier = Modifier
        ) {
            items(folders) { folder ->
                FolderComponent(
                    folder = folder,
                    onLongPress = { checkMode.value = true },
                    checkMode = checkMode,
                    updateDeleteFolder = updateListOfCheckedDirectories,
                    selectedFolder = selectedFolder,
                    selectFolder = selectFolder,
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notes.filter { selectedFolder.value == -1L || it.folderId == selectedFolder.value }) { note ->
                NoteComponent(
                    note = note,
                    editNote = navigateToNewNoteEditor,
                    deleteNote = deleteNote
                )
            }
        }
    }


}

@Composable
fun MultipleFloatingActionButtonFolderNoteCreation(
    showOptions: Boolean,
    navigateToNewFolderEditor: () -> Unit,
    onCreateNewNote: () -> Unit,
    onMainFloatingActionButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MultipleFloatingActionButton(
        modifier = modifier,
        showOptions = showOptions,
        buttonPadding = 24.dp,
        mainFloatingActionButton = {
            LargeFloatingActionButton(onClick = onMainFloatingActionButtonClick) {
                Icon(
                    Icons.Filled.Add,
                    modifier = Modifier.size(48.dp),
                    contentDescription =
                    stringResource(R.string.home_screen_main_floating_action_button_desc)
                )
            }
        }) {
        FloatingActionButton(onClick = navigateToNewFolderEditor) {
            Icon(
                Icons.Rounded.CreateNewFolder,
                contentDescription = stringResource(R.string.create_new_folder_desc)
            )
        }

        FloatingActionButton(onClick = onCreateNewNote) {
            Icon(
                Icons.Rounded.NoteAdd,
                contentDescription = stringResource(R.string.create_new_note_desc)
            )
        }
    }
}

@Composable
fun MultipleFloatingActionButton(
    showOptions: Boolean,
    buttonPadding: Dp,
    modifier: Modifier = Modifier,
    mainFloatingActionButton: @Composable () -> Unit,
    options: @Composable () -> Unit
) {
    val density = LocalDensity.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(buttonPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val offset = 96.dp + buttonPadding
        AnimatedVisibility(
            visible = showOptions,
            enter = slideInVertically {
                with(density) {
                    offset.roundToPx()
                }
            } + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically {
                with(density) {
                    offset.roundToPx()
                }
            } + fadeOut()
        ) {
            Column(
                //modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(buttonPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                options()
            }
        }

        mainFloatingActionButton()
    }
}

private fun getOptions(
    viewModel: HomeViewModel,
    uiState: HomeUiState,
    checkMode: MutableState<Boolean>,
    navigateToUpdateFolderEditor: (Long) -> Unit
): List<OptionVerticalMore> {

    var options = mutableListOf<OptionVerticalMore>()
    if (uiState.listOfCheckedDirectories.isNotEmpty()) {
        options.add(
            OptionVerticalMore(
                action = {
                    if (uiState.listOfCheckedDirectories.isNotEmpty()) {
                        viewModel.removeDirectories(uiState.listOfCheckedDirectories)
                    }
                    checkMode.value = false
                },
                description = "Delete Folders",
                icon = Icons.Filled.Delete
            )
        )
    }

    if (uiState.listOfCheckedDirectories.size == 1) {
        options.add(
            OptionVerticalMore(
                action = {
                    checkMode.value = false
                    viewModel.clearListOfCheckedDirectories()
                    navigateToUpdateFolderEditor(uiState.listOfCheckedDirectories.last())
                },
                description = "Update Folder",
                icon = Icons.Filled.Update
            )
        )
    }
    options.add(
        OptionVerticalMore(
            action = {
                viewModel.singOut()
            },
            description = "SingOut",
            icon = Icons.Filled.Logout
        )
    )
    options.add(
        OptionVerticalMore(
            action = {
                checkMode.value = false
                viewModel.clearListOfCheckedDirectories()
            },
            description = "Cancelar",
            icon = Icons.Filled.Cancel
        )
    )

    return options
}

@Preview
@Composable
fun HomeScreenBodyPreview() {
    val checkMode = remember { mutableStateOf(false) }
    var selectedFolder = remember { mutableStateOf(-1L) }
    HomeScreenBody(
        folders = listOf(),
        notes = listOf(),
        modifier = Modifier.fillMaxSize(),
        updateListOfCheckedDirectories = { _, _ -> },
        checkMode = checkMode,
        onDismissDialogRequest = {},
        onConfirmDialogRequest = {},
        showCreateNoteDialog = false,
        navigateToNewNoteEditor = {},
        deleteNote = {},
        selectedFolder = selectedFolder,
        selectFolder = {}

    )
}


@Preview
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MultipleFloatingActionButtonPreview() {
    var showOptions by remember { mutableStateOf(true) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            MultipleFloatingActionButtonFolderNoteCreation(
                showOptions = showOptions,
                navigateToNewFolderEditor = {},
                onMainFloatingActionButtonClick = { showOptions = !showOptions },
                onCreateNewNote = {}
            )
        }
    ) {

    }
}

@Preview
@Composable
fun TopBarPreview() {
    EnebroNotesTheme() {
        HomeAppBar(
            title = stringResource(id = HomeDestination.titleRes),
            canNavigateBack = true,
            navigateUp = {},
            modifier = Modifier,
            options = listOf()
        )
    }

}


