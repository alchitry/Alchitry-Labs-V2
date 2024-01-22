package com.alchitry.labs2.ui.code_editor.styles.acf

import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.ui.code_editor.EditorToken
import com.alchitry.labs2.ui.code_editor.styles.EditorTokenizer
import com.alchitry.labs2.ui.code_editor.styles.lucid.LucidStyle
import com.alchitry.labs2.ui.code_editor.toEditorToken
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CommonTokenStream

object AcfTokenizer : EditorTokenizer {
    private val operatorRegex = Regex("[]\\[;]+")
    private val capRegex = Regex("[A-Z0-9_]+")

    override fun getTokens(stream: CharStream): List<EditorToken> {
        val lexer = AcfLexer(stream)
        lexer.removeErrorListeners()
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        return tokenStream.tokens.map { token ->
            val style = when (token.type) {
                AcfLexer.Tokens.PULLUP.id, AcfLexer.Tokens.PULLDOWN.id -> LucidStyle.signed
                AcfLexer.Tokens.COMMENT.id, AcfLexer.Tokens.BLOCK_COMMENT.id -> LucidStyle.comment
                AcfLexer.Tokens.FREQ_UNIT.id, AcfLexer.Tokens.INT.id -> LucidStyle.value
                AcfLexer.Tokens.REAL.id -> LucidStyle.realValue
                AcfLexer.Tokens.BASIC_NAME.id -> if (token.text?.let { capRegex.matches(it) } == true) LucidStyle.constant else null
                else -> when (token.text) {
                    "pin", "clock" -> LucidStyle.variable
                    else ->
                        if (token.text?.let { operatorRegex.matches(it) } == true) LucidStyle.operator else null
                }
            }
            token.toEditorToken(style)
        }
    }
}