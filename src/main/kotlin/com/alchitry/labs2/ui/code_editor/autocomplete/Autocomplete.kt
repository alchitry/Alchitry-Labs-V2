@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.alchitry.labs2.ui.code_editor.autocomplete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import com.alchitry.labs2.ui.code_editor.TextPosition
import com.alchitry.labs2.ui.theme.AlchitryTypography
import org.apache.commons.lang3.StringUtils

data class Suggestion(val nextText: String, val quality: Int, val range: OpenEndRange<TextPosition>)

abstract class Autocomplete(protected val state: CodeEditorState) {
    protected var selection: Int by mutableStateOf(0)
    protected var suggestions: List<Suggestion> by mutableStateOf(emptyList())
    protected var overlayPosition: Offset? by mutableStateOf(null)
    var active by mutableStateOf(false)
        protected set

    abstract fun updateSuggestions()

    open fun reset() {
        active = false
        selection = 0
        suggestions = emptyList()
        overlayPosition = null
    }

    fun selectionUp(): Boolean {
        if (!active)
            return false
        selection = if (selection <= 0) suggestions.size - 1 else selection - 1
        return true
    }

    fun selectionDown(): Boolean {
        if (!active)
            return false
        selection = if (selection >= suggestions.size - 1) 0 else selection + 1
        return true
    }

    fun select(): Suggestion? {
        if (active) {
            return suggestions.getOrNull(selection).also { reset() }
        }
        return null
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun suggestionOverlay() {
        if (active && suggestions.isNotEmpty()) {
            overlayPosition?.let { position ->
                Popup(
                    popupPositionProvider = rememberPopupPositionProviderAtPosition(positionPx = position),
                    onDismissRequest = { reset() }
                ) {
                    CompositionLocalProvider(LocalTextStyle provides AlchitryTypography.editor) {
                        Surface(
                            Modifier.offset(x = (-8).dp),
                            tonalElevation = 10.dp,
                            shadowElevation = 4.dp
                        ) {
                            Column(Modifier.width(IntrinsicSize.Max)) {
                                suggestions.forEachIndexed { idx, suggestion ->
                                    val mod = if (idx == selection)
                                        Modifier.background(MaterialTheme.colorScheme.primary)
                                    else Modifier
                                    Box(mod.padding(horizontal = 8.dp).fillMaxWidth()) {
                                        Text(suggestion.nextText)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {

        /**
         * Calculates a modified Levenshtein distance between two strings. The smaller the returned value, the
         * more similar the strings are.
         *
         * @param str The potential match string.
         * @param key The partially typed string.
         * @return A number related to the number of edit operations required to transform the source string
         * into the target string. Returns null if the target string is not found in the source string.
         */
        @JvmStatic
        protected fun calculateEditDistance(str: String, key: String): Int? {
            val letters = key.toCharArray()
            if (letters.isNotEmpty()) {
                var offset = StringUtils.indexOfIgnoreCase(str, letters[0].toString())
                if (offset == -1) return null

                offset++

                var edit = offset * 100

                if (letters[0] != str[offset - 1]) edit++

                for (i in 1..<letters.size) {
                    val idx = StringUtils.indexOfIgnoreCase(str, letters[i].toString(), offset)
                    if (idx == -1 || idx == str.length) return null

                    edit += (idx - offset) * 100
                    offset = idx + 1
                    if (letters[i] != str[idx]) edit += 10
                }
                return edit + str.length - letters.size
            }
            return null
        }
    }
}