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
import com.alchitry.labs2.Log
import com.alchitry.labs2.ui.components.TextTooltipArea
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class SearchAndReplaceMode {
    None, Search, Replace
}

class SearchAndReplaceState(private val editor: AlchitryTextFieldState) {
    private var mode by mutableStateOf(SearchAndReplaceMode.None)
    private var searchFieldState by mutableStateOf(TextFieldValue())
    private var replaceFieldState by mutableStateOf(TextFieldValue())
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
        searchFieldState = if (searchText == null) {
            searchFieldState.copy(selection = TextRange(0, searchFieldState.text.length))
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
        if (editor.isReadOnly) return
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
        val query = searchFieldState.text
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

    fun replaceAll() {
        val pattern = buildSearchRegexOrNull() ?: return
        val text = editor.getText()
        val replacement = if (regex) replaceFieldState.text else Regex.escapeReplacement(replaceFieldState.text)
        val newText = try {
            text.replace(pattern, replacement)
        } catch (e: Exception) {
            Log.error(e.message)
            return
        }
        editor.setText(newText)
    }

    fun replaceOnce() {
        val activeHighlight = searchHighlights.getOrNull(activeIndex ?: return) ?: return
        val newText = if (regex) {
            val pattern = buildSearchRegexOrNull() ?: return
            val text = editor.getTextInRange(activeHighlight.range)
            try {
                text.replace(pattern, replaceFieldState.text)
            } catch (e: Exception) {
                Log.error(e.message)
                return
            }
        } else {
            replaceFieldState.text
        }
        editor.selectionManager.start = activeHighlight.range.endExclusive
        editor.selectionManager.end = activeHighlight.range.endExclusive
        editor.selectionManager.caret = activeHighlight.range.endExclusive
        editor.replaceText(newText, activeHighlight.range)
    }

    private fun setActiveIndex(newIndex: Int) {
        if (searchHighlights.isEmpty()) return
        val boundedNewIndex = newIndex.coerceIn(0, searchHighlights.size - 1)

        activeIndex?.let { oldIndex ->
            val boundedOldIndex = oldIndex.coerceIn(0, searchHighlights.size - 1)
            searchHighlights[boundedOldIndex] = searchHighlights[boundedOldIndex].copy(color = inactiveColor)
        }

        searchHighlights[boundedNewIndex] = searchHighlights[boundedNewIndex].copy(color = activeColor)
        activeIndex = boundedNewIndex

        val range = searchHighlights[boundedNewIndex].range
        editor.selectionManager.start = range.start
        editor.selectionManager.end = range.endExclusive
        editor.selectionManager.caret = range.endExclusive
    }

    fun nextMatch() {
        if (searchHighlights.isEmpty()) return

        // Find the first match strictly after selectionManager.end (wrap to start if none).
        val anchor = editor.selectionManager.end
        val nextIndex = searchHighlights.indexOfFirst { it.range.start > anchor }
            .let { if (it >= 0) it else 0 }

        setActiveIndex(nextIndex)
    }

    fun previousMatch() {
        if (searchHighlights.isEmpty()) return

        // Find the closest match strictly before selectionManager.start (wrap to end if none).
        val anchor = editor.selectionManager.start
        val prevIndex = searchHighlights.indexOfLast { it.range.endExclusive < anchor }
            .let { if (it >= 0) it else (searchHighlights.size - 1) }

        setActiveIndex(prevIndex)
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
                    start..<stop,
                    if (active) activeColor else inactiveColor
                )
            )
        }
        if (!matched) {
            if (searchHighlights.isNotEmpty()) {
                nextMatch()
            } else {
                activeIndex = null
                editor.selectionManager.clearSelection()
            }
        }
    }

    @Composable
    fun drawBar() {
        val searchTextStyle =
            MaterialTheme.typography.bodyMedium // or .copy(fontSize = 14.sp, lineHeight = 18.sp)
        val shape = RoundedCornerShape(percent = 50)
        val scope = rememberCoroutineScope()
        AnimatedVisibility(
            mode != SearchAndReplaceMode.None,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                Modifier
                    .background(AlchitryColors.current.SearchBar)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .onFocusChanged {
                        hasFocus = it.hasFocus
                    }
                    .onPreviewKeyEvent { e ->
                        val key = e.key
                        val ctrl = e.isCtrlPressed
                        val alt = e.isAltPressed
                        val shift = e.isShiftPressed
                        val meta = e.isMetaPressed
                        if (e.type == KeyEventType.KeyDown) {
                            when {
                                key == Key.Escape -> hide()
                                key == Key.Enter -> nextMatch()
                                ctrl && !alt && !shift && !meta && key == Key.F -> showSearch(null)
                                ctrl && !alt && !shift && !meta && key == Key.R -> showReplace(null)
                                else -> return@onPreviewKeyEvent false
                            }
                            return@onPreviewKeyEvent true
                        }
                        false
                    }
            ) {
                Row(
                    Modifier
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffect(grabFocus) {
                        if (grabFocus) focusRequester.requestFocus()
                        grabFocus = false
                    }
                    val interactionSource = remember { MutableInteractionSource() }

                    val innerTextStyle = if (buildSearchRegexOrNull() == null) {
                        searchTextStyle.copy(color = AlchitryColors.current.Error)
                    } else {
                        searchTextStyle.copy(color = MaterialTheme.colorScheme.onSurface)
                    }
                    BasicTextField(
                        value = searchFieldState,
                        onValueChange = {
                            searchFieldState = it
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
                            .focusRequester(focusRequester),
                    ) { innerTextField ->
                        Row(
                            modifier = Modifier
                                .padding(start = 14.dp, end = 10.dp), // you control the right padding here
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box {
                                if (searchFieldState.text.isEmpty()) {
                                    Text("Search", style = searchTextStyle, modifier = Modifier.alpha(0.4f))
                                }
                                innerTextField()
                            }
                            Row(
                                modifier = Modifier.padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AnimatedVisibility(
                                    searchFieldState.text.isNotEmpty(),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(CircleShape)
                                            .clickable {
                                                searchFieldState = TextFieldValue("")
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
                                "${
                                    ((activeIndex ?: 0) + 1).coerceIn(
                                        0,
                                        searchHighlights.size
                                    )
                                }/${searchHighlights.size}",
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
                AnimatedVisibility(
                    mode == SearchAndReplaceMode.Replace,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Row(
                        Modifier
                            .height(IntrinsicSize.Min)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val focusRequester = remember { FocusRequester() }
                        val interactionSource = remember { MutableInteractionSource() }
                        BasicTextField(
                            value = replaceFieldState,
                            onValueChange = {
                                replaceFieldState = it
                            },
                            singleLine = true,
                            textStyle = searchTextStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                            keyboardOptions = KeyboardOptions.Default,
                            keyboardActions = KeyboardActions.Default,
                            interactionSource = interactionSource,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .height(40.dp)
                                .widthIn(min = 400.dp)
                                .clip(shape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .focusRequester(focusRequester),
                        ) { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .padding(start = 14.dp, end = 10.dp), // you control the right padding here
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box {
                                    if (replaceFieldState.text.isEmpty()) {
                                        Text("Replace", style = searchTextStyle, modifier = Modifier.alpha(0.4f))
                                    }
                                    innerTextField()
                                }
                                Row(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AnimatedVisibility(
                                        replaceFieldState.text.isNotEmpty(),
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .clip(CircleShape)
                                                .clickable {
                                                    replaceFieldState = TextFieldValue("")
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
                                }
                            }
                        }

                        AnimatedVisibility(activeIndex != null, enter = fadeIn(), exit = fadeOut()) {
                            Row(
                                modifier = Modifier.padding(start = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SearchBarButton(
                                    "icons/replace_once.svg",
                                    "Replace One"
                                ) { replaceOnce() }
                                SearchBarButton(
                                    "icons/replace_all.svg",
                                    "Replace All"
                                ) { replaceAll() }
                            }

                        }
                    }
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