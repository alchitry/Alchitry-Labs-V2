package com.alchitry.labs2.ui.code_editor.styles.lucid

import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.ui.code_editor.EditorToken
import com.alchitry.labs2.ui.code_editor.styles.EditorTokenizer
import com.alchitry.labs2.ui.code_editor.toEditorToken
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CommonTokenStream

object LucidTokenizer : EditorTokenizer {
    private val operatorRegex = Regex("([*!~+#\\-/:@|&{}?^=><\\]\\[,();]+)|(c\\{|x\\{)")

    override fun getTokens(stream: CharStream): List<EditorToken> {
        val lexer = LucidLexer(stream)
        lexer.removeErrorListeners()
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        return tokenStream.tokens.map { token ->
            val style = when (token.type) {
                LucidLexer.Tokens.SIGNED.id -> LucidStyle.signed
                LucidLexer.Tokens.COMMENT.id, LucidLexer.Tokens.BLOCK_COMMENT.id -> LucidStyle.comment
                LucidLexer.Tokens.HEX.id, LucidLexer.Tokens.BIN.id, LucidLexer.Tokens.DEC.id, LucidLexer.Tokens.INT.id -> LucidStyle.value
                LucidLexer.Tokens.REAL.id -> LucidStyle.realValue
                LucidLexer.Tokens.STRING.id -> LucidStyle.string
                LucidLexer.Tokens.CONST_ID.id -> LucidStyle.constant
                LucidLexer.Tokens.SPACE_ID.id -> LucidStyle.namespace
                LucidLexer.Tokens.FUNCTION_ID.id -> LucidStyle.function
                else -> when (token.text) {
                    "input", "output", "inout", "dff", "sig", "const", "struct", "enum" -> LucidStyle.variable
                    "always", "if", "else", "repeat", "case", "test", "fun" -> LucidStyle.keyword
                    "module", "global", "testBench" -> LucidStyle.module
                    "default" -> LucidStyle.defaultKeyword
                    else ->
                        if (token.text?.let { operatorRegex.matches(it) } == true) LucidStyle.operator else null
                }
            }
            token.toEditorToken(style)
        }
    }
}