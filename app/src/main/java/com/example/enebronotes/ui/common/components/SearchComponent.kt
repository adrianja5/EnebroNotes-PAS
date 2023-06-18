package com.example.enebronotes.ui.common.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enebronotes.R
import com.example.enebronotes.ui.theme.EnebroNotesTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = modifier,
        value = searchText,
        onValueChange = onSearchTextChange,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        leadingIcon = if (searchText.isBlank()) {
            {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_desc)
                )
            }
        } else {
            {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.search_desc)
                    )
                }
            }
        },
        trailingIcon = if (searchText.isNotBlank()) {
            {
                IconButton(onClick = {
                    onSearchClick()
                    keyboardController?.hide()
                }) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.search_desc)
                    )
                }
            }
        } else {
            null
        },
        keyboardOptions = if (searchText.isNotBlank()) {
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            )
        } else {
            KeyboardOptions.Default
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick()
                keyboardController?.hide()
            }
        )
    )
}

@Preview
@Composable
fun SearchTextFieldWithTextPreview() {
    EnebroNotesTheme() {
        SearchTextField(
            modifier = Modifier,
            searchText = "Preview",
            onSearchTextChange = {},
            onSearchClick = {}
        )
    }
}

@Preview
@Composable
fun SearchTextFieldWithoutTextPreview() {
    EnebroNotesTheme() {
        SearchTextField(
            modifier = Modifier,
            searchText = "",
            onSearchTextChange = {},
            onSearchClick = {}
        )
    }
}