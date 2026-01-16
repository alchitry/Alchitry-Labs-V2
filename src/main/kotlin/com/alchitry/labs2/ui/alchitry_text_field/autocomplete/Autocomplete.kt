package com.alchitry.labs2.ui.alchitry_text_field.autocomplete

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
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.ui.alchitry_text_field.SelectionManager
import com.alchitry.labs2.ui.alchitry_text_field.TextPosition
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextProvider
import com.alchitry.labs2.ui.theme.AlchitryTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import kotlin.coroutines.cancellation.CancellationException

data class Suggestion(val nextText: String, val quality: Int, val range: OpenEndRange<TextPosition>)
interface AutocompleteSource : TextProvider {
    val scope: CoroutineScope
    val selectionManager: SelectionManager
    fun textPositionToScreenOffset(position: TextPosition): Offset?
    fun replaceText(newText: String, range: OpenEndRange<TextPosition>)
}

abstract class Autocomplete {
    private var possible: List<String>? = null
    protected var selection: Int by mutableStateOf(0)
    protected var suggestions: List<Suggestion> by mutableStateOf(emptyList())
    protected var overlayPosition: Offset? by mutableStateOf(null)
    private var updateJob: Job? = null
    var active by mutableStateOf(false)
        protected set

    protected abstract suspend fun getPossible(source: AutocompleteSource, offset: Int): List<String>?

    fun updateSuggestions(source: AutocompleteSource) {
        updateJob?.cancel()
        updateJob = source.scope.launch {
            try {
                buildTree(source)
            } catch (e: Exception) {
                if (e is CancellationException) return@launch
                Log.exception(e)
            }
        }
    }

    open fun reset(scope: CoroutineScope) {
        active = false
        selection = 0
        suggestions = emptyList()
        overlayPosition = null
        scope.launch {
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

    fun select(scope: CoroutineScope): Suggestion? {
        if (active) {
            return suggestions.getOrNull(selection).also { reset(scope) }
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

    private suspend fun buildTree(source: AutocompleteSource) {
        val caret = source.selectionManager.caret
        val lineOffset = (0..<caret.line).sumOf { (source.getLine(it)?.length ?: 0) + 1 }
        val offset = lineOffset + caret.offset - 1

        val possible = (if (active) possible else null) ?: getPossible(source, offset)
        this.possible = possible
        if (possible == null) {
            reset(source.scope)
            return
        }

        val line = source.getLine(caret.line) ?: return
        val relevantText = line.take(caret.offset.coerceAtMost(line.length)).getRelevantText()
        if (relevantText.isBlank()) {
            reset(source.scope)
            return
        }
        val pieces = relevantText.split('.')
        val requiredPrefix =
            if (pieces.size > 1) pieces.subList(0, pieces.size - 1).joinToString(".", postfix = ".") else ""

        val start = caret.copy(offset = caret.offset - relevantText.length)
        val end = caret

        suggestions = if (pieces.last().isNotEmpty()) {
            possible.mapNotNull {
                if (!it.startsWith(requiredPrefix)) return@mapNotNull null
                val diff = calculateEditDistance(it, pieces.last()) ?: return@mapNotNull null
                Suggestion(it, diff, start..<end)
            }.sortedBy { it.quality }
        } else {
            possible.mapNotNull {
                if (!it.startsWith(requiredPrefix)) return@mapNotNull null
                Suggestion(it, 0, start..<end)
            }
        }

        active = suggestions.isNotEmpty()

        overlayPosition = source.textPositionToScreenOffset(caret.copy(offset = caret.offset - relevantText.length))
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun suggestionOverlay(source: AutocompleteSource) {
        if (active && suggestions.isNotEmpty()) {
            overlayPosition?.let { position ->
                Popup(
                    popupPositionProvider = rememberPopupPositionProviderAtPosition(positionPx = position),
                    onDismissRequest = { reset(source.scope) }
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
                                                    source.replaceText(suggestion.nextText, suggestion.range)
                                                    reset(source.scope)
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

        fun forFile(file: ProjectFile): Autocomplete? {
            return when (file.language) {
                Languages.Lucid -> LucidAutocomplete(file as SourceFile)
                Languages.ACF -> AcfAutocomplete()
                else -> null
            }
        }
    }
}