package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.project.files.ProjectFile
import com.alchitry.labs.ui.code_editor.TextPosition
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.TerminalNode
import java.util.*

data class Notation(
    val message: String?,
    val range: ClosedRange<TextPosition>,
    val type: NotationType
) {
    override fun toString(): String {
        return "${type.label} at line ${range.start.line} offset ${range.start.offset}${message?.let { ": $it" } ?: ""}"
    }
}

enum class NotationType(val label: String) {
    Error("Error"),
    Warning("Warning"),
    Info("Info"),
    SyntaxError("Syntax error"),
    SyntaxAmbiguity("Syntax ambiguity")
}

class ErrorCollector(val file: ProjectFile) : ErrorListener, ANTLRErrorListener {
    val errors = mutableListOf<Notation>()
    val warnings = mutableListOf<Notation>()
    val infos = mutableListOf<Notation>()
    val syntaxIssues = mutableListOf<Notation>()

    fun new(): ErrorCollector = ErrorCollector(file)

    fun getAllNotations(): List<Notation> = mutableListOf<Notation>().apply {
        addAll(errors)
        addAll(warnings)
        addAll(infos)
        addAll(syntaxIssues)
    }

    private fun getNotation(token: Token, message: String?, type: NotationType) = Notation(
        message = message,
        range = TextPosition(token.line - 1, token.charPositionInLine)..
                TextPosition(token.line - 1, token.charPositionInLine + token.text.length),
        type = type
    )

    private fun getNotation(ctx: ParserRuleContext, message: String?, type: NotationType) = Notation(
        message = message,
        range = TextPosition(ctx.start.line - 1, ctx.start.charPositionInLine)..
                TextPosition(ctx.stop.line - 1, ctx.stop.charPositionInLine + ctx.stop.text.length),
        type = type
    )

    override fun reportError(node: TerminalNode, message: String) {
        errors.add(getNotation(node.symbol, message, NotationType.Error))
    }

    override fun reportError(ctx: ParserRuleContext, message: String) {
        errors.add(getNotation(ctx, message, NotationType.Error))
    }

    override fun reportWarning(node: TerminalNode, message: String) {
        warnings.add(getNotation(node.symbol, message, NotationType.Warning))
    }

    override fun reportWarning(ctx: ParserRuleContext, message: String) {
        warnings.add(getNotation(ctx, message, NotationType.Warning))
    }

    override fun reportInfo(node: TerminalNode, message: String) {
        infos.add(getNotation(node.symbol, message, NotationType.Info))
    }

    override fun reportInfo(ctx: ParserRuleContext, message: String) {
        infos.add(getNotation(ctx, message, NotationType.Info))
    }

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        val token = (offendingSymbol as? Token) ?: return
        syntaxIssues.add(getNotation(token, msg, NotationType.SyntaxError))
    }

    override fun reportAmbiguity(
        recognizer: Parser,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        exact: Boolean,
        ambigAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
        val token = recognizer.tokenStream.get(startIndex)
        syntaxIssues.add(getNotation(token, null, NotationType.SyntaxAmbiguity))
    }

    override fun reportAttemptingFullContext(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        conflictingAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
        //val text = recognizer?.tokenStream?.getText(Interval(startIndex, stopIndex))
        //syntaxIssues.add("Syntax attempting full context at $startIndex to $stopIndex \"$text\"")
    }

    override fun reportContextSensitivity(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        prediction: Int,
        configs: ATNConfigSet?
    ) {
        //syntaxIssues.add("Syntax context sensitivity at $startIndex to $stopIndex")
    }

    fun sort() {
        listOf(syntaxIssues, errors, warnings).forEach { list ->
            list.sortBy { it.range.start }
        }
    }

    fun getReport(): String? {
        if (hasNoIssues)
            return null

        val stringBuilder = StringBuilder()

        stringBuilder.append("Issues detected in ${file.name}:\n")

        (syntaxIssues + errors + warnings).sortedBy { it.range.start }.forEach {
            stringBuilder.append("    $it\n")
        }

        return stringBuilder.toString()
    }

    fun printErrors() {
        errors.forEach {
            println(it)
        }
    }

    val hasNoIssues: Boolean get() = hasNoSyntaxIssues && hasNoErrors && hasNoWarnings

    val hasNoErrors: Boolean get() = errors.isEmpty()
    val hasNoWarnings: Boolean get() = warnings.isEmpty()
    val hasNoSyntaxIssues: Boolean get() = syntaxIssues.isEmpty()
    val hasErrors: Boolean get() = errors.isNotEmpty()
    val hasWarnings: Boolean get() = warnings.isNotEmpty()
    val hasSyntaxIssues: Boolean get() = syntaxIssues.isNotEmpty()
}