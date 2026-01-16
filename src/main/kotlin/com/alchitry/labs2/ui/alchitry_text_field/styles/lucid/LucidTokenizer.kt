package com.alchitry.labs2.ui.alchitry_text_field.styles.lucid

import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.ui.alchitry_text_field.EditorToken
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.toEditorToken
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CommonTokenStream

object LucidTokenizer : TextTokenizer {
    private val operatorRegex = Regex("([*!~+#\\-/:@|&{}?^=><\\]\\[,()]+)|(c\\{|x\\{)")

    override fun getTokens(stream: CharStream): List<EditorToken> {
        val lexer = LucidLexer(stream).apply { removeErrorListeners() }
        lexer.removeErrorListeners()
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        return tokenStream.tokens.map { token ->
            val style = when (token.type) {
                LucidLexer.Tokens.SIGNED -> LucidStyle.signed
                LucidLexer.Tokens.COMMENT, LucidLexer.Tokens.BLOCK_COMMENT -> LucidStyle.comment
                LucidLexer.Tokens.HEX, LucidLexer.Tokens.BIN, LucidLexer.Tokens.DEC, LucidLexer.Tokens.INT -> LucidStyle.value
                LucidLexer.Tokens.REAL -> LucidStyle.realValue
                LucidLexer.Tokens.STRING -> LucidStyle.string
                LucidLexer.Tokens.CONST_ID -> LucidStyle.constant
                LucidLexer.Tokens.SPACE_ID -> LucidStyle.namespace
                LucidLexer.Tokens.FUNCTION_ID -> LucidStyle.function
                LucidLexer.Tokens.SEMICOLON -> LucidStyle.deadCode
                else -> when (token.text) {
                    "input", "output", "inout", "dff", "sig", "const", "struct", "enum" -> LucidStyle.variable
                    "always", "if", "else", "repeat", "case", "test", "fun" -> LucidStyle.keyword
                    "module", "global", "testbench" -> LucidStyle.module
                    "default" -> LucidStyle.defaultKeyword
                    else ->
                        if (token.text?.let { operatorRegex.matches(it) } == true) LucidStyle.operator else null
                }
            }
            token.toEditorToken(style)
        }
    }
}