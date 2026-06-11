package com.alchitry.labs2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.decodeToImageVector
import org.jetbrains.compose.resources.decodeToSvgPainter

private fun readResourceBytes(resourcePath: String): ByteArray {
    val classLoader = Thread.currentThread().contextClassLoader ?: error("No context class loader")
    val resource = classLoader.getResourceAsStream(resourcePath)
        ?: error("Resource not found: $resourcePath")
    return resource.readAllBytes()
}

@Composable
fun painterResource(resourcePath: String): Painter =
    when (resourcePath.substringAfterLast(".")) {
        "svg" -> rememberSvgResource(resourcePath)
        "xml" -> rememberVectorXmlResource(resourcePath)
        else -> rememberBitmapResource(resourcePath)
    }

@Composable
private fun rememberBitmapResource(path: String): Painter {
    return remember(path) { BitmapPainter(readResourceBytes(path).decodeToImageBitmap()) }
}

@Composable
private fun rememberVectorXmlResource(path: String): Painter {
    val density = LocalDensity.current
    return rememberVectorPainter(
        remember(density, path) { readResourceBytes(path).decodeToImageVector(density) }
    )
}

@Composable
private fun rememberSvgResource(path: String): Painter {
    val density = LocalDensity.current
    return remember(density, path) { readResourceBytes(path).decodeToSvgPainter(density) }
}
