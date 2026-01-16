package com.alchitry.labs2.ui.alchitry_text_field.styles.acf

import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.ui.alchitry_text_field.styles.BracketIndenter
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextProvider
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.Token

class AcfFormatter : BracketIndenter() {

    override fun formatAll(textProvider: TextProvider): String {
        return removeSemicolons(super.formatAll(textProvider))
    }

    private fun List<Token>.nextFromChannel(start: Int, channel: Int): Token? {
        return subList(start + 1, size).firstOrNull { it.channel == channel }
    }

    private fun List<Token>.previousFromChannel(start: Int, channel: Int): Token? {
        return subList(0, start).lastOrNull { it.channel == channel }
    }

    private fun removeSemicolons(text: String): String {
        val lexer = AcfLexer(CharStreams.fromString(text))
        val tokens = lexer.allTokens
        val tokenText = tokens.map { it.text }.toMutableList()

        tokens.forEachIndexed { idx, it ->
            when (it.type) {
                AcfLexer.Tokens.SEMICOLON -> {
                    if (tokens.previousFromChannel(
                            idx,
                            AcfLexer.Channels.DEFAULT_TOKEN_CHANNEL
                        )?.type == AcfLexer.Tokens.NL ||
                        tokens.nextFromChannel(
                            idx,
                            AcfLexer.Channels.DEFAULT_TOKEN_CHANNEL
                        )?.type == AcfLexer.Tokens.NL
                    ) {
                        tokenText[idx] = ""
                    }
                }

                AcfLexer.Tokens.NL -> {
                    tokenText[idx] = "\n"
                }
            }
        }

        return tokenText.joinToString(separator = "")
    }

}