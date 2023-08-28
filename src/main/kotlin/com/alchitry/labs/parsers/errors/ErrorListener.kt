package com.alchitry.labs.parsers.errors

import com.alchitry.labs.project.files.ProjectFile
import com.alchitry.labs.ui.code_editor.TextPosition
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.TerminalNode

interface ErrorListener {
    val file: ProjectFile
    val errors: List<Notation>
    val warnings: List<Notation>
    val infos: List<Notation>

    fun reportError(node: TerminalNode, message: String)
    fun reportWarning(node: TerminalNode, message: String)
    fun reportInfo(node: TerminalNode, message: String)
    fun reportError(ctx: ParserRuleContext, message: String)
    fun reportWarning(ctx: ParserRuleContext, message: String)
    fun reportInfo(ctx: ParserRuleContext, message: String)

    fun getAllNotations() = (errors + warnings + infos).sortedBy { it.range.start }

    fun getReport(): String? {
        if (hasNoMessages)
            return null

        val stringBuilder = StringBuilder()

        stringBuilder.append("Issues detected in ${file.name}:\n")

        getAllNotations().forEach {
            stringBuilder.append("    $it\n")
        }

        return stringBuilder.toString()
    }

    fun printErrors() {
        errors.forEach {
            println(it)
        }
    }

    val hasNoIssues: Boolean get() = hasNoErrors && hasNoWarnings
    val hasNoMessages: Boolean get() = hasNoErrors && hasNoWarnings && hasNoInfos

    val hasNoErrors: Boolean get() = errors.isEmpty()
    val hasNoWarnings: Boolean get() = warnings.isEmpty()
    val hasErrors: Boolean get() = errors.isNotEmpty()
    val hasWarnings: Boolean get() = warnings.isNotEmpty()
    val hasInfos: Boolean get() = infos.isNotEmpty()
    val hasNoInfos: Boolean get() = infos.isEmpty()

    companion object {
        fun getNotation(token: Token, message: String?, type: NotationType) = Notation(
            message = message,
            range = TextPosition(token.line - 1, token.charPositionInLine)..
                    TextPosition(token.line - 1, token.charPositionInLine + token.text.length),
            type = type
        )

        fun getNotation(ctx: ParserRuleContext, message: String?, type: NotationType) = Notation(
            message = message,
            range = TextPosition(ctx.start.line - 1, ctx.start.charPositionInLine)..
                    TextPosition(ctx.stop.line - 1, ctx.stop.charPositionInLine + ctx.stop.text.length),
            type = type
        )
    }
}

