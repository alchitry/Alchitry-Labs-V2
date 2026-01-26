package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.animation.*
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
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.focus.onFocusChanged
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
    var activeIndex: Int? = null
    var grabFocus by mutableStateOf(false)
    var hasFocus by mutableStateOf(false)

    private val activeColor get() = AlchitryColors.current.SearchMatchHighlight.copy(alpha = 0.6f)
    private val inactiveColor get() = AlchitryColors.current.SearchMatchHighlight.copy(alpha = 0.3f)

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

    fun hasFocus(): Boolean = hasFocus

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

    fun nextMatch() {
        val activeIndex = activeIndex ?: return
        searchHighlights[activeIndex] = searchHighlights[activeIndex].copy(
            color = inactiveColor
        )
        val nextIndex = if (activeIndex + 1 >= searchHighlights.size) 0 else activeIndex + 1
        searchHighlights[nextIndex] = searchHighlights[nextIndex].copy(
            color = activeColor
        )
        this.activeIndex = nextIndex

        editor.selectionManager.start = searchHighlights[nextIndex].range.start
        editor.selectionManager.end = searchHighlights[nextIndex].range.endInclusive
        editor.selectionManager.caret = searchHighlights[nextIndex].range.endInclusive
    }

    fun previousMatch() {
        val activeIndex = activeIndex ?: return
        searchHighlights[activeIndex] = searchHighlights[activeIndex].copy(
            color = inactiveColor
        )
        val nextIndex = if (activeIndex - 1 < 0) searchHighlights.size - 1 else activeIndex - 1
        searchHighlights[nextIndex] = searchHighlights[nextIndex].copy(
            color = activeColor
        )
        this.activeIndex = nextIndex

        editor.selectionManager.start = searchHighlights[nextIndex].range.start
        editor.selectionManager.end = searchHighlights[nextIndex].range.endInclusive
        editor.selectionManager.caret = searchHighlights[nextIndex].range.endInclusive
    }

    fun updateHighlights(moveCaret: Boolean = true) {
        val text = editor.getText()
        searchHighlights.clear()
        val searchRegex = buildSearchRegexOrNull() ?: return
        var matched = false
        searchRegex.findAll(text).forEachIndexed { index, it ->
            val start = textOffsetToPosition(text, it.range.first)
            val stop = textOffsetToPosition(text, it.range.last + 1)
            val active = if (!matched) {
                if (editor.selectionManager.start <= start) {
                    matched = true
                    activeIndex = index
                    if (moveCaret) {
                        editor.selectionManager.start = start
                        editor.selectionManager.end = stop
                        editor.selectionManager.caret = stop
                    }
                    true
                } else false
            } else false
            searchHighlights.add(
                HighlightAnnotation(
                    start..stop,
                    if (active) activeColor else inactiveColor
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
                    .height(IntrinsicSize.Min)
                    .onFocusChanged {
                        hasFocus = it.hasFocus
                    }
                    .onPreviewKeyEvent { e ->
                        if (e.type == KeyEventType.KeyDown && e.key == Key.Escape) {
                            hide()
                            true
                        } else false
                    },
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
                        .height(40.dp)
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
                            .padding(start = 14.dp, end = 10.dp), // you control the right padding here
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box {
                            if (textFieldState.text.isEmpty()) {
                                Text("Search", style = searchTextStyle, modifier = Modifier.alpha(0.4f))
                            }
                            innerTextField()
                        }
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AnimatedVisibility(textFieldState.text.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
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
                                        modifier = Modifier.size(14.dp),
                                        tint = LocalContentColor.current.copy(alpha = 0.6f)
                                    )
                                }
                            }
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
                    }
                }

                AnimatedVisibility(searchHighlights.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    Row(
                        modifier = Modifier.padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${((activeIndex ?: 0) + 1).coerceIn(0, searchHighlights.size)}/${searchHighlights.size}",
                            Modifier.alpha(0.6f)
                        )
                        SearchBarButton(
                            "icons/chevron-up.svg",
                            "Previous"
                        ) { previousMatch() }
                        SearchBarButton(
                            "icons/chevron-down.svg",
                            "Next"
                        ) { nextMatch() }
                    }

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

@Composable
private fun SearchBarButton(
    iconPath: String,
    description: String,
    onClick: () -> Unit,
) {
    TextTooltipArea(
        text = description
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(2.dp)
                .clip(RoundedCornerShape(percent = 20))
                .clickable(
                    onClick = onClick,
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