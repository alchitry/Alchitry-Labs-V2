package com.alchitry.labs2.ui.alchitry_text_field.styles

import androidx.compose.ui.text.AnnotatedString
import com.alchitry.labs2.ui.alchitry_text_field.EditorToken
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CharStreams

interface TextTokenizer {
    fun getTokens(stream: CharStream): List<EditorToken>
    fun getTokens(text: String): List<EditorToken> = getTokens(CharStreams.fromString(text))

    /**
     * Annotates the given text. Assumes the text doesn't contain new lines.
     */
    fun annotate(text: String, tokens: List<EditorToken> = getTokens(text)): AnnotatedString {
        return AnnotatedString.Builder().apply {
            append(text)
            tokens.forEach {
                if (it.style != null)
                    addStyle(it.style, it.range.start.offset, it.range.endInclusive.offset)
            }
        }.toAnnotatedString()
    }
}