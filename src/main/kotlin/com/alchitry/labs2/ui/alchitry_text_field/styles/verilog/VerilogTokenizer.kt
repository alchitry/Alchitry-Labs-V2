package com.alchitry.labs2.ui.alchitry_text_field.styles.verilog

import com.alchitry.labs2.parsers.grammar.VerilogLexer
import com.alchitry.labs2.parsers.grammar.VerilogLexer.Tokens
import com.alchitry.labs2.ui.alchitry_text_field.EditorToken
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.styles.lucid.LucidStyle
import com.alchitry.labs2.ui.alchitry_text_field.toEditorToken
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CommonTokenStream

object VerilogTokenizer : TextTokenizer {
    private val operatorRegex = Regex("[*!~+#\\-/:@|&{}?^=><\\]\\[,();]+")

    override fun getTokens(stream: CharStream): List<EditorToken> {
        val lexer = VerilogLexer(stream).apply { removeErrorListeners() }
        lexer.removeErrorListeners()
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        return tokenStream.tokens.map { token ->
            val style = when (token.type) {
                Tokens.SIGNED -> LucidStyle.signed
                Tokens.BLOCK_COMMENT, Tokens.LINE_COMMENT -> LucidStyle.comment

                Tokens.HEX_VALUE, Tokens.BINARY_VALUE, Tokens.OCTAL_VALUE,
                Tokens.DECIMAL_BASE, Tokens.HEX_BASE, Tokens.BINARY_BASE,
                Tokens.OCTAL_BASE, Tokens.UNSIGNED_NUMBER -> LucidStyle.value

                Tokens.REAL -> LucidStyle.realValue
                Tokens.STRING -> LucidStyle.string
                Tokens.MODULE, Tokens.ENDMODULE -> LucidStyle.module
                Tokens.FUNCTION, Tokens.ENDFUNCTION -> LucidStyle.function

                Tokens.ALWAYS, Tokens.BEGIN, Tokens.END,
                Tokens.ENDCASE, Tokens.ASSIGN, Tokens.IF,
                Tokens.IFNONE, Tokens.FOR, Tokens.FOREVER,
                Tokens.CASE, Tokens.CASEX, Tokens.CASEZ,
                Tokens.POSEDGE, Tokens.NEGEDGE, Tokens.GENERATE,
                Tokens.ENDGENERATE, Tokens.WHILE -> LucidStyle.defaultKeyword

                Tokens.INPUT, Tokens.OUTPUT, Tokens.INOUT,
                Tokens.WIRE, Tokens.UWIRE, Tokens.REG,
                Tokens.TRIREG, Tokens.PARAMETER, Tokens.LOCALPARAM -> LucidStyle.variable

                else ->
                    if (token.text?.let { operatorRegex.matches(it) } == true) LucidStyle.operator else null
            }

            token.toEditorToken(style)
        }
    }
}