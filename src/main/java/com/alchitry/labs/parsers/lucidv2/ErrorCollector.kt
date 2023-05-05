package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.errors.ErrorListener
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.TerminalNode
import java.util.*

class ErrorCollector : ErrorListener, ANTLRErrorListener {
    val errors = mutableListOf<String>()
    val warnings = mutableListOf<String>()
    val debugs = mutableListOf<String>()
    val syntaxIssues = mutableListOf<String>()

    override fun reportError(node: TerminalNode, message: String) {
        errors.add("${node.text}: $message")
    }

    override fun reportError(ctx: ParserRuleContext, message: String) {
        errors.add("${ctx.text}: $message")
    }

    override fun reportWarning(node: TerminalNode, message: String) {
        warnings.add("${node.text}: $message")
    }

    override fun reportWarning(ctx: ParserRuleContext, message: String) {
        warnings.add("${ctx.text}: $message")
    }

    override fun reportDebug(node: TerminalNode, message: String) {
        debugs.add("${node.text}: $message")
    }

    override fun reportDebug(ctx: ParserRuleContext, message: String) {
        debugs.add("${ctx.text}: $message")
    }

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        syntaxIssues.add("Syntax error: $msg")
    }

    override fun reportAmbiguity(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        exact: Boolean,
        ambigAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
        println("Syntax ambiguity at $startIndex to $stopIndex")
    }

    override fun reportAttemptingFullContext(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        conflictingAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
        println("Syntax attempting full context at $startIndex to $stopIndex")
    }

    override fun reportContextSensitivity(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        prediction: Int,
        configs: ATNConfigSet?
    ) {
        println("Syntax context sensitivity at $startIndex to $stopIndex")
    }

    val hasNoIssues: Boolean get() = hasNoSyntaxIssues && hasNoErrors && hasNoWarnings

    val hasNoErrors: Boolean get() {
        errors.forEach {
            println(it)
        }
        return errors.isEmpty()
    }
    val hasNoWarnings: Boolean get() {
        warnings.forEach {
            println(it)
        }
        return warnings.isEmpty()
    }
    val hasNoSyntaxIssues: Boolean get() {
        syntaxIssues.forEach {
            println(it)
        }
        return syntaxIssues.isEmpty()
    }
    val hasErrors: Boolean get() = errors.isNotEmpty()
    val hasWarnings: Boolean get() = warnings.isNotEmpty()
    val hasSyntaxIssues: Boolean get() = syntaxIssues.isNotEmpty()
}