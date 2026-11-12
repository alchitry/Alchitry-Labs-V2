package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.runBlocking
import kotlin.test.*

internal class EditorStateTests {
    @Test
    fun lineLayoutCachesRepeatedConstraintsAndWrapsAtNarrowWidths() {
        val density = Density(1f)
        val textMeasurer = TextMeasurer(
            createFontFamilyResolver(),
            density,
            LayoutDirection.Ltr
        )
        val line = AlchitryLineState(
            AnnotatedString("a fairly long line of text"),
            textMeasurer,
            TextStyle(fontSize = 16.sp)
        )

        val narrowConstraints = Constraints(maxWidth = 40)
        val narrowLayout = line.layout(narrowConstraints)!!
        assertTrue(narrowLayout.lineCount > 1)
        assertSame(narrowLayout, line.layout(narrowConstraints))

        val wideLayout = line.layout(Constraints())!!
        assertEquals(1, wideLayout.lineCount)
        assertNotSame(narrowLayout, wideLayout)
    }

    @Test
    fun editorScrollStateClampsOffsetsAndShrinkingContent() {
        val scrollState = EditorScrollState()
        scrollState.updateBounds(maxValue = 100, viewportSize = 40)

        assertEquals(60f, scrollState.dispatchRawDelta(60f))
        assertEquals(60, scrollState.value)
        assertEquals(0f, scrollState.dispatchRawDelta(10f))
        assertEquals(-60f, scrollState.dispatchRawDelta(-100f))

        scrollState.dispatchRawDelta(80f)
        scrollState.updateBounds(maxValue = 20, viewportSize = 80)
        assertEquals(20, scrollState.value)
        scrollState.updateBounds(maxValue = 0, viewportSize = 0)
        assertEquals(0, scrollState.value)
    }

    @Test
    fun editorScrollbarAdapterUsesScrollBounds() = runBlocking {
        val scrollState = EditorScrollState()
        scrollState.updateBounds(maxValue = 120, viewportSize = 60)
        val adapter = EditorScrollbarAdapter(scrollState)

        assertEquals(120f, adapter.maxScrollOffset(containerSize = 60))
        adapter.scrollTo(containerSize = 60, scrollOffset = 120f)
        assertEquals(120, scrollState.value)
    }

    @Test
    fun desktopKeyMappingPreservesWindowsAndMacShortcuts() {
        assertEquals(KeyCommand.SELECT_ALL, map(Key.A, ctrl = true))
        assertEquals(KeyCommand.LEFT_WORD, map(Key.DirectionLeft, ctrl = true))
        assertEquals(KeyCommand.SELECT_LEFT_WORD, map(Key.DirectionLeft, ctrl = true, shift = true))
        assertEquals(KeyCommand.DELETE_PREV_WORD, map(Key.Backspace, ctrl = true))
        assertEquals(KeyCommand.HOME, map(Key.MoveHome, ctrl = true))
        assertNull(map(Key.B, ctrl = true))
        assertEquals(
            KeyCommand.NEW_LINE,
            EditorKeyMapping.mapNativeFallback(java.awt.event.KeyEvent.VK_ENTER, ctrl = false, shift = false)
        )
        assertEquals(
            KeyCommand.HOME,
            EditorKeyMapping.mapNativeFallback(java.awt.event.KeyEvent.VK_HOME, ctrl = true, shift = false)
        )
        assertEquals(
            KeyCommand.END,
            EditorKeyMapping.mapNativeFallback(java.awt.event.KeyEvent.VK_END, ctrl = true, shift = false)
        )

        assertEquals(KeyCommand.SELECT_ALL, map(Key.A, meta = true, isMac = true))
        assertEquals(KeyCommand.LEFT_WORD, map(Key.DirectionLeft, alt = true, isMac = true))
        assertEquals(KeyCommand.LINE_START, map(Key.DirectionLeft, meta = true, isMac = true))
        assertEquals(
            KeyCommand.SELECT_LINE_END,
            map(Key.DirectionRight, shift = true, meta = true, isMac = true)
        )
        assertEquals(KeyCommand.DELETE_FROM_LINE_START, map(Key.Backspace, meta = true, isMac = true))
        assertEquals(KeyCommand.LINE_START, map(Key.A, ctrl = true, isMac = true))
        assertEquals(KeyCommand.SELECT_LINE_END, map(Key.E, ctrl = true, shift = true, isMac = true))
        assertEquals(KeyCommand.PREV_PARAGRAPH, map(Key.DirectionUp, alt = true, isMac = true))
    }

    private fun map(
        key: Key,
        ctrl: Boolean = false,
        alt: Boolean = false,
        shift: Boolean = false,
        meta: Boolean = false,
        isMac: Boolean = false
    ) = EditorKeyMapping.map(key, ctrl, alt, shift, meta, isMac)
}