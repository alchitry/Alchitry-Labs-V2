package com.alchitry.labs.ui.code_editor

import androidx.compose.ui.text.AnnotatedString
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams

interface EditorTokenizer {
    fun getTokens(stream: CharStream): List<StyleToken>
    fun getTokens(text: String): List<StyleToken> = getTokens(CharStreams.fromString(text))

    /**
     * Annotates the given text. Assumes the text doesn't contain new lines.
     */
    fun annotate(text: String): AnnotatedString {
        val tokens = getTokens(text)
        return AnnotatedString.Builder().apply {
            append(text)
            tokens.forEach {
                if (it.style != null)
                    addStyle(it.style, it.start.offset, it.end.offset)
            }
        }.toAnnotatedString()
    }
}