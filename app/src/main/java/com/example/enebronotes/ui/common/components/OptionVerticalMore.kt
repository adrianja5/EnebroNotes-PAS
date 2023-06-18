package com.example.enebronotes.ui.common.components

import androidx.compose.ui.graphics.vector.ImageVector


data class OptionVerticalMore(
    val description: String,
    val action: () -> Unit,
    val icon: ImageVector
) {

}