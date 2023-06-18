package com.example.enebronotes.ui.common.shapes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class FolderShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width / 20
        val height = size.width / 16 * 0.8f
        val path = Path().apply {
            moveTo(width.times(8.59f), height.times(0.59f))
            cubicTo(
                width.times(8.21f), height.times(0.21f),
                width.times(7.7f), height.times(0f),
                width.times(7.17f), height.times(0f)
            )
            lineTo(width.times(2f), height.times(0f))
            cubicTo(
                width.times(0.9f), height.times(0f),
                width.times(0.01f), height.times(0.9f),
                width.times(0.01f), height.times(2f)
            )
            lineTo(width.times(0f), height.times(14f))
            relativeCubicTo(
                width.times(0f), height.times(1.1f),
                width.times(0.9f), height.times(2f),
                width.times(2f), height.times(2f)
            )
            relativeLineTo(width.times(16f), height.times(0f))
            relativeCubicTo(
                width.times(1.1f), height.times(0f),
                width.times(2f), height.times(-0.9f),
                width.times(2f), height.times(-2f)
            )
            lineTo(width.times(2f * 2 + 16f), height.times(4f))
            cubicTo(
                width.times(20f), height.times(2.9f),
                width.times(19.1f), height.times(2f),
                width.times(18f), height.times(2f)
            )
            relativeLineTo(width.times(-8f), height.times(0f))
            close()

            // TODO: Shape a parte para poner el color de la nota
            /*moveTo(width.times(8.59f), height.times(0.59f))
            relativeLineTo(width.times(8f), height.times(0f))
            relativeCubicTo(
                width.times(1f), height.times(0f),
                width.times(1.41f), height.times(0.39f),
                width.times(1.41f), height.times(1.41f)
            )
            relativeLineTo(width.times(-8f), height.times(0f))
            close()*/
        }
        return Outline.Generic(path)
    }
}

class FolderBackShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width / 20
        val height = size.width / 16 * 0.8f
        val path = Path().apply {
            moveTo(width.times(8.59f), height.times(0.59f))
            relativeLineTo(width.times(8f), height.times(0f))
            relativeCubicTo(
                width.times(1f), height.times(0f),
                width.times(1.41f), height.times(0.39f),
                width.times(1.41f), height.times(1.41f)
            )
            relativeLineTo(width.times(-8f), height.times(0f))
            close()
        }
        return Outline.Generic(path)
    }
}

@Preview
@Composable
fun FolderShapeBorderPreview() {
    Surface(
        modifier = Modifier
            .size(128.dp),
        shape = FolderShape(),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.Black)
    ) {

    }
}

@Preview
@Composable
fun FolderShapeColorPreview() {
    Surface(
        modifier = Modifier
            .size(128.dp),
        shape = FolderShape(),
        color = Color.Cyan,
    ) {
    }
}

@Preview
@Composable
fun FolderBackShapeColorPreview() {
    Surface(
        modifier = Modifier
            .size(128.dp),
        shape = FolderBackShape(),
        color = Color.Cyan,
    ) {
    }
}

@Preview
@Composable
fun FullFolderShapeColorPreview() {
    val size = 128.dp
    Box(modifier = Modifier.size(size, size * 0.8f)) {
        Surface(
            modifier = Modifier
                .size(size),
            shape = FolderShape(),
            color = Color.Cyan,
        ) {
        }
        Surface(
            modifier = Modifier
                .size(size),
            shape = FolderBackShape(),
            color = Color.Red,
        ) {
        }
    }
}
