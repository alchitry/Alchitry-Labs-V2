package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScope
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Constraints
import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.VerilogLexer
import com.alchitry.labs2.parsers.notations.LineAction
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.ui.alchitry_text_field.autocomplete.Autocomplete
import com.alchitry.labs2.ui.alchitry_text_field.styles.CodeFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log10

class CodeEditorState(val file: ProjectFile) {
    val textFieldState = AlchitryTextFieldState(
        autocomplete = Autocomplete.forFile(file),
        codeFormatter = CodeFormatter.forLanguage(file.language),
        tokenizer = file.textTokenizer,
        onTextChanged = { onTextChanged() },
        onDoubleClicK = { onDoubleClick(it) },
        onReplaceText = { _, _ -> onReplaceText() },
    )
    var saveJob: Job? = null
    var lineActions: Map<Int, List<LineAction>>? = null
    var maxLineActions = 0
    val scope = textFieldState.scope
    var showReadOnlyDialog by mutableStateOf(false)
    private var gutterDigits = 0
    var gutterWidth = 0
        private set

    init {
        Project.current?.currentNotationCollectorForFile(file)?.let { collector ->
            updateNotations(collector)
        }
        scope.launch {
            textFieldState.setText(file.readText())
            textFieldState.styler.updateStyle()
        }
    }

    @Composable
    fun subscribe() {
        val project by Project.currentFlow.collectAsState()
        LaunchedEffect(project) {
            project?.notationCollectorFlowForFile(file)?.collect {
                it?.let { updateNotations(it) }

                textFieldState.styler.updateStyle()
                textFieldState.invalidate()
            }
        }

        textFieldState.subscribe()
    }

    fun onTextChanged() {
        if (!file.isReadOnly) scope.launch {
            file.textTokenizer
            saveJob?.cancelAndJoin()
            saveJob = launch {
                delay(100)
                file.writeText(textFieldState.getText())
                Project.current?.queueNotationsUpdate()
            }
        }
    }

    fun onDoubleClick(offset: Offset): Boolean {
        val token = textFieldState.offsetToToken(offset) ?: return false

        when (file.language) {
            Languages.ACF -> when (token.token.type) {
                AcfLexer.Tokens.BLOCK_COMMENT, AcfLexer.Tokens.COMMENT -> {
                    textFieldState.selectWordAt(textFieldState.screenOffsetToTextPosition(offset))
                    return true
                }
            }

            Languages.Lucid -> when (token.token.type) {
                LucidLexer.Tokens.BLOCK_COMMENT, LucidLexer.Tokens.COMMENT, LucidLexer.Tokens.STRING -> {
                    textFieldState.selectWordAt(textFieldState.screenOffsetToTextPosition(offset))
                    return true
                }
            }

            Languages.Verilog -> when (token.token.type) {
                VerilogLexer.Tokens.BLOCK_COMMENT, VerilogLexer.Tokens.LINE_COMMENT, VerilogLexer.Tokens.STRING -> {
                    textFieldState.selectWordAt(textFieldState.screenOffsetToTextPosition(offset))
                    return true
                }
            }

            else -> {}
        }

        return false
    }

    fun onReplaceText(): Boolean {
        if (file.isReadOnly) {
            showReadOnlyDialog = true
        }
        return file.isReadOnly
    }

    /**
     * Updates the notations and line actions based on the given [notationCollector].
     *
     * @param notationCollector The notation collector from which to retrieve the notations and line actions.
     */
    fun updateNotations(notationCollector: NotationCollector) {
        textFieldState.notations.clear()
        textFieldState.notations.addAll(notationCollector.getAllNotations())
        lineActions = notationCollector.getLineActions()
        maxLineActions = lineActions?.values?.maxOfOrNull { actions -> actions.size } ?: 0
    }

    private fun Int.length() = when (this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }

    fun LazyLayoutMeasureScope.layoutGutter() {
        val lineHeight = textFieldState.lines.firstOrNull()?.lineHeight.let {
            if (it == null || it == 0) textFieldState.defaultLineHeight() else it
        }

        val maxDigits = textFieldState.lines.size.length()
        val placeables = measure(-maxDigits - 1, Constraints(maxHeight = lineHeight))
        gutterWidth = placeables.maxOf { it.width }
        gutterDigits = maxDigits
    }
}