package com.alchitry.labs.parsers.errors

import com.alchitry.labs.project.files.ProjectFile
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.TerminalNode
import java.util.*

class ErrorCollector(override val file: ProjectFile) : ErrorListener, ANTLRErrorListener {
    override val errors = mutableListOf<Notation>()
    override val warnings = mutableListOf<Notation>()
    override val infos = mutableListOf<Notation>()

    override fun reportError(node: TerminalNode, message: String) {
        errors.add(ErrorListener.getNotation(node.symbol, message, NotationType.Error))
    }

    override fun reportError(ctx: ParserRuleContext, message: String) {
        errors.add(ErrorListener.getNotation(ctx, message, NotationType.Error))
    }

    override fun reportWarning(node: TerminalNode, message: String) {
        warnings.add(ErrorListener.getNotation(node.symbol, message, NotationType.Warning))
    }

    override fun reportWarning(ctx: ParserRuleContext, message: String) {
        warnings.add(ErrorListener.getNotation(ctx, message, NotationType.Warning))
    }

    override fun reportInfo(node: TerminalNode, message: String) {
        infos.add(ErrorListener.getNotation(node.symbol, message, NotationType.Info))
    }

    override fun reportInfo(ctx: ParserRuleContext, message: String) {
        infos.add(ErrorListener.getNotation(ctx, message, NotationType.Info))
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
        errors.add(ErrorListener.getNotation(token, msg, NotationType.SyntaxError))
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
        errors.add(ErrorListener.getNotation(token, null, NotationType.SyntaxAmbiguity))
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
}