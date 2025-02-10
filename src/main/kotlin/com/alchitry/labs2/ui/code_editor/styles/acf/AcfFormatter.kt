package com.alchitry.labs2.ui.code_editor.styles.acf

import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import com.alchitry.labs2.ui.code_editor.styles.BracketIndenter
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.Token

class AcfFormatter(codeEditorState: CodeEditorState) : BracketIndenter(codeEditorState) {

    override fun formatAll(): String {
        return removeSemicolons(super.formatAll())
    }

    private fun List<Token>.nextFromChannel(start: Int, channel: AcfLexer.Channels): Token? {
        return subList(start + 1, size).firstOrNull { it.channel == channel.id }
    }

    private fun List<Token>.previousFromChannel(start: Int, channel: AcfLexer.Channels): Token? {
        return subList(0, start).lastOrNull { it.channel == channel.id }
    }

    private fun removeSemicolons(text: String): String {
        val lexer = AcfLexer(CharStreams.fromString(text))
        val tokens = lexer.allTokens
        val tokenText = tokens.map { it.text }.toMutableList()

        tokens.forEachIndexed { idx, it ->
            when (it.type) {
                AcfLexer.Tokens.SEMICOLON.id -> {
                    if (tokens.previousFromChannel(
                            idx,
                            AcfLexer.Channels.DEFAULT_TOKEN_CHANNEL
                        )?.type == AcfLexer.Tokens.NL.id ||
                        tokens.nextFromChannel(
                            idx,
                            AcfLexer.Channels.DEFAULT_TOKEN_CHANNEL
                        )?.type == AcfLexer.Tokens.NL.id
                    ) {
                        tokenText[idx] = ""
                    }
                }

                AcfLexer.Tokens.NL.id -> {
                    tokenText[idx] = "\n"
                }
            }
        }

        return tokenText.joinToString(separator = "")
    }

}