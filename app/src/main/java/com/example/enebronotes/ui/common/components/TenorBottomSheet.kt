package com.example.enebronotes.ui.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enebronotes.R
import com.example.enebronotes.ui.theme.EnebroNotesTheme

@Composable
fun TenorBottomSheet(
    onDismissRequest: () -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onGifClicked: (String) -> Unit,
    gifsUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState
    ) {
        TenorBottomSheetBody(
            modifier = Modifier.fillMaxSize(),
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onSearchClick = onSearchClick,
            gifsUrls = gifsUrls,
            onGifClicked = onGifClicked
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TenorBottomSheetBody(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onGifClicked: (String) -> Unit,
    gifsUrls: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.tenor_gifs), fontSize = 32.sp)
        Spacer(Modifier.height(16.dp))
        SearchTextField(
            modifier = Modifier.fillMaxWidth(),
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onSearchClick = onSearchClick
        )
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(gifsUrls) { gifUrl ->
                ShowMedia(gifUrl, modifier.clickable {
                    onGifClicked(gifUrl)
                })
            }
        }
    }
}

@Preview
@Composable
fun TenorBottomSheetBodyPreview() {
    EnebroNotesTheme() {
        Surface() {
            TenorBottomSheetBody(
                modifier = Modifier.fillMaxSize(),
                searchText = "",
                onSearchTextChange = {},
                onSearchClick = {},
                gifsUrls = listOf(),
                onGifClicked = {}
            )
        }
    }
}
