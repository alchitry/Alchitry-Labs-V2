package com.alchitry.labs2.ui.code_editor.styles.verilog

import com.alchitry.labs2.parsers.grammar.VerilogLexer
import com.alchitry.labs2.parsers.grammar.VerilogLexer.Tokens
import com.alchitry.labs2.ui.code_editor.EditorToken
import com.alchitry.labs2.ui.code_editor.styles.EditorTokenizer
import com.alchitry.labs2.ui.code_editor.styles.lucid.LucidStyle
import com.alchitry.labs2.ui.code_editor.toEditorToken
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CommonTokenStream

object VerilogTokenizer : EditorTokenizer {
    private val operatorRegex = Regex("[*!~+#\\-/:@|&{}?^=><\\]\\[,();]+")

    override fun getTokens(stream: CharStream): List<EditorToken> {
        val lexer = VerilogLexer(stream)
        lexer.removeErrorListeners()
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        return tokenStream.tokens.map { token ->
            val style = when (token.type) {
                Tokens.SIGNED.id -> LucidStyle.signed
                Tokens.BLOCK_COMMENT.id, Tokens.LINE_COMMENT.id -> LucidStyle.comment

                Tokens.HEX_VALUE.id, Tokens.BINARY_VALUE.id, Tokens.OCTAL_VALUE.id,
                Tokens.DECIMAL_BASE.id, Tokens.HEX_BASE.id, Tokens.BINARY_BASE.id,
                Tokens.OCTAL_BASE.id, Tokens.UNSIGNED_NUMBER.id -> LucidStyle.value

                Tokens.REAL.id -> LucidStyle.realValue
                Tokens.STRING.id -> LucidStyle.string
                Tokens.MODULE.id, Tokens.ENDMODULE.id -> LucidStyle.module
                Tokens.FUNCTION.id, Tokens.ENDFUNCTION.id -> LucidStyle.function

                Tokens.ALWAYS.id, Tokens.BEGIN.id, Tokens.END.id,
                Tokens.ENDCASE.id, Tokens.ASSIGN.id, Tokens.IF.id,
                Tokens.IFNONE.id, Tokens.FOR.id, Tokens.FOREVER.id,
                Tokens.CASE.id, Tokens.CASEX.id, Tokens.CASEZ.id,
                Tokens.POSEDGE.id, Tokens.NEGEDGE.id, Tokens.GENERATE.id,
                Tokens.ENDGENERATE.id, Tokens.WHILE.id -> LucidStyle.defaultKeyword

                Tokens.INPUT.id, Tokens.OUTPUT.id, Tokens.INOUT.id,
                Tokens.WIRE.id, Tokens.UWIRE.id, Tokens.REG.id,
                Tokens.TRIREG.id -> LucidStyle.variable

                else ->
                    if (token.text?.let { operatorRegex.matches(it) } == true) LucidStyle.operator else null
            }

            token.toEditorToken(style)
        }
    }
}