package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.runtime.*
import kotlin.math.roundToInt

class EditorScrollState(initialValue: Int = 0) : ScrollableState {
    private var currentValue by mutableIntStateOf(initialValue.coerceAtLeast(0))
    private var currentMaxValue by mutableIntStateOf(0)
    private var currentViewportSize by mutableIntStateOf(0)

    val value: Int get() = currentValue
    val maxValue: Int get() = currentMaxValue
    val viewportSize: Int get() = currentViewportSize

    private val scrollableState = ScrollableState { delta ->
        val oldValue = currentValue
        currentValue = (oldValue + delta).roundToInt().coerceIn(0, currentMaxValue)
        (currentValue - oldValue).toFloat()
    }

    override val isScrollInProgress: Boolean
        get() = scrollableState.isScrollInProgress

    override suspend fun scroll(scrollPriority: MutatePriority, block: suspend ScrollScope.() -> Unit) {
        scrollableState.scroll(scrollPriority, block)
    }

    override fun dispatchRawDelta(delta: Float): Float = scrollableState.dispatchRawDelta(delta)

    fun updateBounds(maxValue: Int, viewportSize: Int) {
        currentMaxValue = maxValue.coerceAtLeast(0)
        currentViewportSize = viewportSize.coerceAtLeast(0)
        currentValue = currentValue.coerceIn(0, currentMaxValue)
    }

    suspend fun scrollTo(value: Int) {
        scrollableState.scroll {
            scrollBy(value.coerceIn(0, currentMaxValue) - currentValue.toFloat())
        }
    }

    suspend fun animateScrollTo(value: Int) {
        animateScrollBy(value.coerceIn(0, currentMaxValue) - currentValue.toFloat())
    }
}

internal class EditorScrollbarAdapter(
    private val scrollState: EditorScrollState
) : ScrollbarAdapter {
    override val scrollOffset: Double
        get() = scrollState.value.toDouble()

    override val contentSize: Double
        get() = scrollState.viewportSize.toDouble() + scrollState.maxValue.toDouble()

    override val viewportSize: Double
        get() = scrollState.viewportSize.toDouble()

    override suspend fun scrollTo(scrollOffset: Double) {
        scrollState.scrollTo(scrollOffset.roundToInt())
    }
}

@Composable
fun rememberEditorScrollbarAdapter(scrollState: EditorScrollState): ScrollbarAdapter =
    remember(scrollState) { EditorScrollbarAdapter(scrollState) }