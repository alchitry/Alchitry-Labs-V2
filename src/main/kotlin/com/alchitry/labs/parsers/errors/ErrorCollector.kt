package com.alchitry.labs.parsers.errors

import com.alchitry.kotlinmultiplatform.BitSet
import com.alchitry.labs.project.files.ProjectFile
import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.atn.ATNConfigSet
import org.antlr.v4.kotlinruntime.dfa.DFA
import org.antlr.v4.kotlinruntime.tree.TerminalNode

// TODO: Fix concurrency issues during simulation
class ErrorCollector(override val file: ProjectFile) : ErrorListener, ANTLRErrorListener {
    override val errors = mutableListOf<Notation>()
    override val warnings = mutableListOf<Notation>()
    override val infos = mutableListOf<Notation>()

    fun clear() {
        errors.clear()
        warnings.clear()
        infos.clear()
    }

    override fun reportError(node: TerminalNode, message: String) {
        node.symbol?.let {
            errors.add(ErrorListener.getNotation(it, message, NotationType.Error))
        }
    }

    override fun reportError(ctx: ParserRuleContext, message: String) {
        ErrorListener.getNotation(ctx, message, NotationType.Error)?.let { errors.add(it) }
    }

    override fun reportWarning(node: TerminalNode, message: String) {
        node.symbol?.let {
            warnings.add(ErrorListener.getNotation(it, message, NotationType.Warning))
        }
    }

    override fun reportWarning(ctx: ParserRuleContext, message: String) {
        ErrorListener.getNotation(ctx, message, NotationType.Warning)?.let { warnings.add(it) }
    }

    override fun reportInfo(node: TerminalNode, message: String) {
        node.symbol?.let {
            infos.add(ErrorListener.getNotation(it, message, NotationType.Info))
        }
    }

    override fun reportInfo(ctx: ParserRuleContext, message: String) {
        ErrorListener.getNotation(ctx, message, NotationType.Info)?.let { infos.add(it) }
    }

    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        val token = (offendingSymbol as? Token) ?: return
        errors.add(ErrorListener.getNotation(token, msg, NotationType.SyntaxError))
    }

    override fun reportAmbiguity(
        recognizer: Parser,
        dfa: DFA,
        startIndex: Int,
        stopIndex: Int,
        exact: Boolean,
        ambigAlts: BitSet,
        configs: ATNConfigSet
    ) {
        val token = recognizer.tokenStream?.get(startIndex) ?: return
        errors.add(ErrorListener.getNotation(token, null, NotationType.SyntaxAmbiguity))
    }

    override fun reportAttemptingFullContext(
        recognizer: Parser,
        dfa: DFA,
        startIndex: Int,
        stopIndex: Int,
        conflictingAlts: BitSet,
        configs: ATNConfigSet
    ) {
        //val text = recognizer?.tokenStream?.getText(Interval(startIndex, stopIndex))
        //syntaxIssues.add("Syntax attempting full context at $startIndex to $stopIndex \"$text\"")
    }

    override fun reportContextSensitivity(
        recognizer: Parser,
        dfa: DFA,
        startIndex: Int,
        stopIndex: Int,
        prediction: Int,
        configs: ATNConfigSet
    ) {
        //syntaxIssues.add("Syntax context sensitivity at $startIndex to $stopIndex")
    }
}