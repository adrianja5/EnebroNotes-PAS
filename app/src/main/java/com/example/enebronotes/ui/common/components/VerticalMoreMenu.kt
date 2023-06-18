package com.example.enebronotes.ui.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun VerticalMoreMenu(
    options: List<OptionVerticalMore>,
    modifier: Modifier,
    onClickItem: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { onClickItem() }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it.description) },
                    onClick = {
                        it.action()
                        onClickItem()
                    },
                    leadingIcon = { Icon(it.icon, it.description) }
                )
            }
        }
    }
}


/*
DropdownMenuItem(
            text = { Text("Edit") },
            onClick = { /* Handle edit! */ },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = null
                )
            })
        DropdownMenuItem(
            text = { Text("Settings") },
            onClick = { /* Handle settings! */ },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = null
                )
            })
        Divider()
        DropdownMenuItem(
            text = { Text("Send Feedback") },
            onClick = { /* Handle send feedback! */ },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Email,
                    contentDescription = null
                )
            },
            trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
    }
* */