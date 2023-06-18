import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enebronotes.R
import com.example.enebronotes.ui.AppViewModelProvider
import com.example.enebronotes.ui.common.components.ValidColors
import com.example.enebronotes.ui.screens.folder.CreateFolderViewModel
import com.example.enebronotes.ui.screens.folder.FolderDetails
import com.example.enebronotes.ui.screens.folder.FolderUiState
import com.example.enebronotes.ui.screens.navigation.NavigationDestination
import kotlinx.coroutines.launch


object CreateFolderDestination : NavigationDestination {
    override val route = "create_new_folder"
    override val titleRes = R.string.create_new_folder_desc
}

@Composable
fun CreateFolderAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            val coroutineScope = rememberCoroutineScope()
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
fun CreateFolderScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean,
    viewModel: CreateFolderViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CreateFolderAppBar(
                title = stringResource(id = CreateFolderDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                modifier = Modifier
            )
        }
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
                    navigateBack()
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

@Composable
fun CreateFolderBody(
    folderUiState: FolderUiState,
    onFolderValueChange: (FolderDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    setColor: (String) -> Unit,
    selectedColor: ValidColors?
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ItemInputForm(
                folderDetails = folderUiState.folderDetails,
                onValueChange = onFolderValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                selectedColor = selectedColor,
                setColor = setColor
            )
            Button(
                onClick = onSaveClick,
                enabled = folderUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun ItemInputForm(
    folderDetails: FolderDetails,
    modifier: Modifier = Modifier,
    onValueChange: (FolderDetails) -> Unit = {},
    selectedColor: ValidColors?,
    setColor: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        var expanded by remember { mutableStateOf(false) }
        var nameColor by remember { mutableStateOf("SELECT") }

        OutlinedTextField(
            value = folderDetails.title,
            onValueChange = { onValueChange(folderDetails.copy(title = it)) },
            label = { Text("Folder Title") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = modifier,
            enabled = true,
            singleLine = true
        )

        ColorOptionsCard(selectedColor, setColor)

    }


}

@Composable
fun ColorOptionsCard(selectedColor: ValidColors?, setColor: (String) -> Unit) {

    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Folder Colors", fontSize = 16.sp)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 36.dp),
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(ValidColors.values()) { color ->
                    val selected =
                        if (selectedColor != null) selectedColor.color == color.color else false
                    ColorOption(color, selected, setColor)
                }
            }
        }
    }
}

@Composable
fun ColorOption(color: ValidColors, selected: Boolean, setColor: (String) -> Unit) {
    Button(
        onClick = { setColor(color.name) },
        modifier = Modifier.size(40.dp),
        colors = ButtonDefaults.buttonColors(Color(color.color)),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (selected) {
            Icon(
                Icons.Filled.Check,
                "color",
                Modifier.size(16.dp),
                tint = Color.Black
            )
        }
    }

}

@Preview
@Composable
fun ColorOptionsCardPreview() {
    ColorOptionsCard(
        selectedColor = null,
        setColor = { color -> },
    )
}

@Preview
@Composable
fun ColorOptionPreview() {
    ColorOption(ValidColors.BLUE, true, setColor = { color -> })
}


