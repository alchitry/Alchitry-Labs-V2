package com.alchitry.labs2.ui.cache

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Caches the content of the Composable function [content] and only re-draws it when the size changes.
 * This should not be used to cache dynamic content as it won't refresh until resized.
 *
 * @param modifier The modifier to be applied to the [Box] composable that holds the cached image.
 * This should have some size modifier attached so the size is known.
 * @param content The Composable function to be cached.
 */
@Composable
fun Cached(modifier: Modifier, content: @Composable () -> Unit) {
    val density = LocalDensity.current

    Box(modifier.drawWithCache {
        if (size.isEmpty())
            return@drawWithCache onDrawBehind { }

        val image = ImageComposeScene(
            size.width.toInt(),
            size.height.toInt(),
            density = density,
            content = content
        ).use { it.render().toComposeImageBitmap() }

        onDrawBehind {
            drawImage(image)
        }
    })
}

/**
 * Displays an image using the provided [painter].
 * This image is rendered off the main thread and a cached version is stretched to fit until a new render is ready.
 * This is particularly useful for rendering slow SVGs that don't need re-rendering each frame.
 *
 * @param painter The [Painter] object representing the image.
 * @param modifier The modifier for the image.
 * @param alpha The alpha value for the image. Default is [DefaultAlpha].
 * @param colorFilter The [ColorFilter] to apply to the image.
 */
@Composable
fun CachedImage(
    painter: Painter,
    modifier: Modifier = Modifier,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    var image: ImageBitmap? by remember { mutableStateOf(null) }
    var size: IntSize? by remember { mutableStateOf(null) }

    val density = LocalDensity.current
    val layout = LocalLayoutDirection.current

    var renderJob: Job? by remember { mutableStateOf(null) }
    LaunchedEffect(size) {
        renderJob?.cancelAndJoin()
        size?.let {
            if (it.width <= 0 || it.height <= 0) {
                image = null
                return@LaunchedEffect
            }
            if (it.width == image?.width && it.height == image?.height)
                return@LaunchedEffect
            renderJob = launch(Dispatchers.Main) { // using Default causes occasional crashes :(
                val bitmap = ImageBitmap(it.width, it.height)
                Canvas(bitmap).apply {
                    val fSize = it.toSize()
                    CanvasDrawScope().draw(density, layout, this, fSize) {
                        with(painter) {
                            draw(fSize, alpha = alpha, colorFilter = colorFilter)
                        }
                    }
                }
                image = bitmap
            }
        }
    }

    Box(
        modifier
            .onGloballyPositioned { size = it.size }
            .drawBehind {
                image?.let {
                    drawImage(
                        image = it,
                        dstSize = IntSize(this.size.width.roundToInt(), this.size.height.roundToInt())
                    )
                }
            }
    ) {
        // use a real image as the cache is being populated to prevent pop-in
        if (image == null) {
            Image(painter, null, Modifier.fillMaxSize(), alpha = alpha, colorFilter = colorFilter)
        }
    }
}

