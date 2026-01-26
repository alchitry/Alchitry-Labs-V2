package com.alchitry.labs2.ui.alchitry_text_field.styles.acf

import com.alchitry.labs2.parsers.acf.AcfExtractor
import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.ui.alchitry_text_field.*
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.styles.lucid.LucidStyle
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CommonTokenStream

object AcfTokenizer : TextTokenizer {
    private val operatorRegex = Regex("[(){}\\]\\[;,]+")
    private val capRegex = Regex("[A-Z0-9_]+")


    override fun getTokens(stream: CharStream): List<EditorToken> {
        val lexer = AcfLexer(stream)
        lexer.removeErrorListeners()
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        val newTokens = mutableListOf<EditorToken>()

        tokenStream.tokens.forEach { token ->
            if (token.type == AcfLexer.Tokens.NATIVE_BLOCK) {
                val text = token.text ?: return@forEach

                newTokens.add(
                    EditorToken(
                        styleToken = StyleToken(
                            token.textPositionAtOffset(0)..<token.textPositionAtOffset("native".length),
                            LucidStyle.keyword
                        ),
                        token = token
                    )
                )

                val bracketOffset = text.indexOf("{") ?: return@forEach

                newTokens.add(
                    EditorToken(
                        styleToken = StyleToken(
                            token.textPositionAtOffset(bracketOffset).run { this..<copy(offset = offset + 1) },
                            LucidStyle.operator
                        ),
                        token = token
                    )
                )


                val nativeText = text.substring(bracketOffset + 1)
                val matches = AcfExtractor.acfNativeRegex.findAll(nativeText)
                val indexOffset = bracketOffset + 1
                var lastIndex = 0

                for (match in matches) {
                    if (match.range.first > lastIndex) {
                        newTokens.add(
                            EditorToken(
                                styleToken = StyleToken(
                                    token.textPositionAtOffset(indexOffset + lastIndex)..<token.textPositionAtOffset(
                                        indexOffset + match.range.first
                                    ),
                                    LucidStyle.string
                                ),
                                token = token
                            )
                        )
                    }

                    val startOffset = match.value.indexOf("(") + match.range.first
                    val endOffset = match.value.lastIndexOf(")") + match.range.first

                    newTokens.add(
                        EditorToken(
                            styleToken = StyleToken(
                                token.textPositionAtOffset(match.range.first + indexOffset)..<
                                        token.textPositionAtOffset(startOffset + indexOffset),
                                LucidStyle.keyword
                            ),
                            token = token
                        )
                    )

                    newTokens.add(
                        EditorToken(
                            styleToken = StyleToken(
                                token.textPositionAtOffset(startOffset + indexOffset)
                                    .run { this..<copy(offset = offset + 1) },
                                LucidStyle.operator
                            ),
                            token = token
                        )
                    )

                    newTokens.add(
                        EditorToken(
                            styleToken = StyleToken(
                                token.textPositionAtOffset(endOffset + indexOffset)
                                    .run { this..<copy(offset = offset + 1) },
                                LucidStyle.operator
                            ),
                            token = token
                        )
                    )

                    val startPosition = token.textPositionAtOffset(startOffset + indexOffset + 1)
                    getTokens(
                        match.value.substring(
                            startOffset + 1 - match.range.first,
                            endOffset - match.range.first
                        )
                    ).forEach {
                        val offsetStart = TextPosition(
                            startPosition.line + it.range.start.line,
                            if (it.range.start.line == 0) startPosition.offset + it.range.start.offset else it.range.start.offset
                        )
                        val offsetEnd = TextPosition(
                            startPosition.line + it.range.endExclusive.line,
                            if (it.range.endExclusive.line == 0) startPosition.offset + it.range.endExclusive.offset else it.range.endExclusive.offset
                        )
                        newTokens.add(
                            EditorToken(
                                StyleToken(
                                    range = offsetStart..<offsetEnd,
                                    style = it.style
                                ),
                                token = it.token
                            )
                        )
                    }

                    lastIndex = match.range.last + 1
                }

                if (lastIndex < nativeText.length) {
                    newTokens.add(
                        EditorToken(
                            styleToken = StyleToken(
                                token.textPositionAtOffset(indexOffset + lastIndex)..<token.textPositionAtOffset(
                                    indexOffset + nativeText.length
                                ),
                                LucidStyle.string
                            ),
                            token = token
                        )
                    )
                }


                return@forEach
            }
            val style = when (token.type) {
                AcfLexer.Tokens.COMMENT, AcfLexer.Tokens.BLOCK_COMMENT -> LucidStyle.comment
                AcfLexer.Tokens.FREQ_UNIT, AcfLexer.Tokens.INT -> LucidStyle.value
                AcfLexer.Tokens.REAL -> LucidStyle.realValue
                AcfLexer.Tokens.BASIC_NAME -> if (token.text?.let { capRegex.matches(it) } == true) LucidStyle.constant else null
                else -> when (token.text) {
                    "pin", "clock" -> LucidStyle.variable
                    "native" -> LucidStyle.keyword
                    else ->
                        if (token.text?.let { operatorRegex.matches(it) } == true) LucidStyle.operator else null
                }
            }
            newTokens.add(token.toEditorToken(style))
        }
        return newTokens
    }
}