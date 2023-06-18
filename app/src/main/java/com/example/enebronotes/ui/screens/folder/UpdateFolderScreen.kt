package com.example.enebronotes.ui.screens.folder


import CreateFolderAppBar
import CreateFolderBody
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enebronotes.R
import com.example.enebronotes.ui.AppViewModelProvider
import com.example.enebronotes.ui.screens.navigation.NavigationDestination
import kotlinx.coroutines.launch

object FolderUpdateDestination : NavigationDestination {
    override val route = "update_folder"
    override val titleRes = R.string.update_folder_title
    const val folderIdArg = "folderId"
    val routeWithArgs = "${route}/{$folderIdArg}"
}


@Composable
fun UpdateFolderScreen(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateFolderViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CreateFolderAppBar(
                title = stringResource(id = FolderUpdateDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        CreateFolderBody(
            folderUiState = viewModel.folderUiState,
            onFolderValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateUp()
                }
            },
            modifier = Modifier
                .background(color = Color.Transparent)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            selectedColor = viewModel.selectedColor,
            setColor = viewModel::setColor,
        )
    }

}

