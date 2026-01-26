package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.components.TextTooltipArea
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class SearchAndReplaceMode {
    None, Search, Replace
}

class SearchAndReplaceState(private val editor: AlchitryTextFieldState) {
    private var mode by mutableStateOf(SearchAndReplaceMode.None)
    private var textFieldState by mutableStateOf(TextFieldValue())
    private var caseSensitive by mutableStateOf(false)
    private var regex by mutableStateOf(false)
    private var wholeWord by mutableStateOf(false)
    val searchHighlights = SnapshotStateList<HighlightAnnotation>()
    var grabFocus by mutableStateOf(false)

    context(scope: DrawScope)
    fun drawHighlights() {
        if (mode == SearchAndReplaceMode.None) return
        searchHighlights.forEach { it.draw(editor) }
    }

    private fun show(searchText: String?, mode: SearchAndReplaceMode) {
        textFieldState = if (searchText == null) {
            textFieldState.copy(selection = TextRange(0, textFieldState.text.length))
        } else {
            TextFieldValue(searchText, selection = TextRange(searchText.length))
        }
        this.mode = mode
        grabFocus = true
        updateHighlights()
    }

    fun showSearch(searchText: String?) {
        show(searchText, SearchAndReplaceMode.Search)
    }

    fun showReplace(searchText: String?) {
        show(searchText, SearchAndReplaceMode.Replace)
    }

    fun isShown(): Boolean = mode != SearchAndReplaceMode.None

    fun hide() {
        mode = SearchAndReplaceMode.None
        editor.focusRequester.requestFocus()
    }

    private fun textOffsetToPosition(text: String, offset: Int): TextPosition {
        val o = offset.coerceIn(0, text.length)

        var line = 0
        var lineStart = 0
        var i = 0

        while (i < o) {
            val c = text[i]
            if (c == '\n') {
                line++
                lineStart = i + 1
            }
            i++
        }

        val column = o - lineStart
        return TextPosition(line = line, offset = column)
    }

    private fun buildSearchRegexOrNull(): Regex? {
        val query = textFieldState.text
        if (query.isEmpty()) return null

        val options = buildSet {
            if (!caseSensitive) add(RegexOption.IGNORE_CASE)
        }

        val basePattern = if (regex) query else Regex.escape(query)
        val finalPattern = if (wholeWord) "\\b(?:$basePattern)\\b" else basePattern

        return try {
            Regex(finalPattern, options)
        } catch (_: Exception) {
            null // invalid pattern
        }
    }

    fun updateHighlights() {
        val text = editor.getText()
        searchHighlights.clear()
        val searchRegex = buildSearchRegexOrNull() ?: return
        searchRegex.findAll(text).forEach {
            val start = textOffsetToPosition(text, it.range.start)
            val stop = textOffsetToPosition(text, it.range.last + 1)
            searchHighlights.add(
                HighlightAnnotation(
                    start..stop,
                    AlchitryColors.current.SearchMatchHighlight.copy(alpha = 0.3f)
                )
            )
        }
    }

    @Composable
    fun drawBar() {
        AnimatedVisibility(mode == SearchAndReplaceMode.Search, enter = expandVertically(), exit = shrinkVertically()) {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(grabFocus) {
                if (grabFocus) focusRequester.requestFocus()
                grabFocus = false
            }
            val scope = rememberCoroutineScope()
            Row(
                Modifier
                    .background(AlchitryColors.current.SearchBar)
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                val shape = RoundedCornerShape(percent = 50)
                val searchTextStyle =
                    MaterialTheme.typography.bodyMedium // or .copy(fontSize = 14.sp, lineHeight = 18.sp)
                val innerTextStyle = if (buildSearchRegexOrNull() == null) {
                    searchTextStyle.copy(color = AlchitryColors.current.Error)
                } else {
                    searchTextStyle.copy(color = MaterialTheme.colorScheme.onSurface)
                }
                BasicTextField(
                    value = textFieldState,
                    onValueChange = {
                        textFieldState = it
                        updateHighlights()
                    },
                    singleLine = true,
                    textStyle = innerTextStyle,
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions.Default,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .height(34.dp)
                        .widthIn(min = 400.dp)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .focusRequester(focusRequester)
                        .onPreviewKeyEvent { e ->
                            if (e.type == KeyEventType.KeyDown && e.key == Key.Escape) {
                                hide()
                                true
                            } else false
                        },
                ) { innerTextField ->
                    Row(
                        modifier = Modifier
                            .padding(start = 14.dp, end = 6.dp), // you control the right padding here
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box {
                            if (textFieldState.text.isEmpty()) {
                                Text("Search", style = searchTextStyle, modifier = Modifier.alpha(0.4f))
                            }
                            innerTextField()
                        }

                        if (textFieldState.text.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp) // tight, predictable
                                    .clip(CircleShape)
                                    .clickable {
                                        textFieldState = TextFieldValue("")
                                        scope.launch {
                                            delay(100)
                                            focusRequester.requestFocus()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource("icons/close.svg"),
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SearchBarToggle(
                        value = caseSensitive,
                        onValueChange = {
                            caseSensitive = it
                            updateHighlights()
                        },
                        "icons/case_sensitive.svg",
                        "Case Sensitive"
                    )
                    SearchBarToggle(
                        value = wholeWord,
                        onValueChange = {
                            wholeWord = it
                            updateHighlights()
                        },
                        "icons/whole_word.svg",
                        "Whole Word"
                    )

                    SearchBarToggle(
                        value = regex,
                        onValueChange = {
                            regex = it
                            updateHighlights()
                        },
                        "icons/regex.svg",
                        "Regular Expression"
                    )
                }


                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(percent = 20))
                        .clickable(
                            onClick = { mode = SearchAndReplaceMode.None },
                            role = Role.Button,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource("icons/close.svg"),
                        "Close",
                        modifier = Modifier.matchParentSize().padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBarToggle(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    iconPath: String,
    description: String
) {
    TextTooltipArea(
        text = description
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(2.dp)
                .clip(RoundedCornerShape(percent = 20))
                .background(if (value) AlchitryColors.current.Accent.copy(alpha = 0.8f) else Color.Transparent)
                .clickable(
                    onClick = { onValueChange(!value) },
                    role = Role.Button,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(iconPath),
                description,
                modifier = Modifier.matchParentSize().padding(4.dp),
            )
        }
    }
}