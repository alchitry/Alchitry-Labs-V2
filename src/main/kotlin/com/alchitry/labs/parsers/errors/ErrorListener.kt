package com.alchitry.labs.parsers.errors

import com.alchitry.labs.ui.code_editor.TextPosition
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.tree.TerminalNode

interface ErrorListener {
    fun reportError(node: TerminalNode, message: String)
    fun reportWarning(node: TerminalNode, message: String)
    fun reportInfo(node: TerminalNode, message: String)
    fun reportError(ctx: ParserRuleContext, message: String)
    fun reportWarning(ctx: ParserRuleContext, message: String)
    fun reportInfo(ctx: ParserRuleContext, message: String)

    companion object {
        fun getNotation(token: Token, message: String?, type: NotationType) = Notation(
            message = message,
            range = TextPosition(token.line - 1, token.charPositionInLine)..
                    TextPosition(token.line - 1, token.charPositionInLine + ((token.text?.length) ?: 0)),
            type = type
        )

        fun getNotation(ctx: ParserRuleContext, message: String?, type: NotationType): Notation? {
            val start = ctx.start ?: return null
            val stop = ctx.stop ?: return null
            return Notation(
                message = message,
                range = TextPosition(start.line - 1, start.charPositionInLine)..
                        TextPosition(stop.line - 1, stop.charPositionInLine + (stop.text?.length ?: 0)),
                type = type
            )
        }
    }
}

