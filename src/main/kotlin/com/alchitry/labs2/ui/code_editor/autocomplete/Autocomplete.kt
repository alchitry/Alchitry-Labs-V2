package com.alchitry.labs2.ui.code_editor.autocomplete

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.alchitry.labs2.Log
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import com.alchitry.labs2.ui.code_editor.TextPosition
import com.alchitry.labs2.ui.theme.AlchitryTypography
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import kotlin.coroutines.cancellation.CancellationException

data class Suggestion(val nextText: String, val quality: Int, val range: OpenEndRange<TextPosition>)

abstract class Autocomplete(protected val state: CodeEditorState) {
    private var possible: List<String>? = null
    protected var selection: Int by mutableStateOf(0)
    protected var suggestions: List<Suggestion> by mutableStateOf(emptyList())
    protected var overlayPosition: Offset? by mutableStateOf(null)
    private var updateJob: Job? = null
    var active by mutableStateOf(false)
        protected set

    protected abstract suspend fun getPossible(offset: Int): List<String>?

    fun updateSuggestions() {
        updateJob?.cancel()
        updateJob = state.scope.launch {
            try {
                buildTree()
            } catch (e: Exception) {
                if (e is CancellationException) return@launch
                Log.exception(e)
            }
        }
    }

    open fun reset() {
        active = false
        selection = 0
        suggestions = emptyList()
        overlayPosition = null
        state.scope.launch {
            updateJob?.cancelAndJoin()
            possible = null
        }
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

    protected fun String.getRelevantText(): String {
        for (i in length - 1 downTo 0) {
            if (!this[i].isLetterOrDigit() && this[i] != '_' && this[i] != '.' && this[i] != '$') {
                return substring(i + 1)
            }
        }
        return this
    }

    private suspend fun buildTree() {
        val caret = state.selectionManager.caret
        val lineOffset = state.lines.subList(0, caret.line).sumOf { it.text.length + 1 }
        val offset = lineOffset + caret.offset - 1

        val possible = (if (active) possible else null) ?: getPossible(offset)
        this.possible = possible
        if (possible == null) {
            reset()
            return
        }

        val line = state.lines.getOrNull(caret.line)?.text?.text ?: return
        val relevantText = line.substring(0, caret.offset.coerceAtMost(line.length)).getRelevantText()
        if (relevantText.isBlank()) {
            reset()
            return
        }
        val pieces = relevantText.split('.')

        val start = caret.copy(offset = caret.offset - relevantText.length)
        val end = caret

        suggestions = possible.mapNotNull {
            val diff = calculateEditDistance(it, pieces.last()) ?: return@mapNotNull null
            Suggestion(it, diff, start..<end)
        }.sortedBy { it.quality }.also {
            active = it.isNotEmpty()
        }

        overlayPosition = state.textPositionToScreenOffset(caret.copy(offset = caret.offset - relevantText.length))
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
                                    Box(
                                        mod
                                            .clickable {
                                                if (selection == idx) {
                                                    state.replaceText(suggestion.nextText, suggestion.range)
                                                    reset()
                                                } else {
                                                    selection = idx
                                                }
                                            }
                                            .padding(horizontal = 8.dp).fillMaxWidth()) {
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