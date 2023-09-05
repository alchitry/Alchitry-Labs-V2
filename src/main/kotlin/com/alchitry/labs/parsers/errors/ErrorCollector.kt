package com.alchitry.labs.parsers.errors

import com.alchitry.kotlinmultiplatform.BitSet
import com.alchitry.labs.project.files.ProjectFile
import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.atn.ATNConfigSet
import org.antlr.v4.kotlinruntime.dfa.DFA
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/***
 * ErrorCollector used during initial parse and subsequent tree walks.
 *
 * Children collectors can be created using the createChild() method.
 * All child errors are reported to the parent, but parent errors are
 * not reported to children.
 *
 * This is not thread safe.
 * Each thread should have its own child collector and the read methods
 * on a parent should only be called once all children have completed.
 */
class ErrorCollector private constructor(
    val file: ProjectFile,
    val name: String,
    private val parent: ErrorCollector?
) : ErrorListener,
    ANTLRErrorListener {
    constructor(file: ProjectFile) : this(file, file.name, null)

    private val children = mutableListOf<ErrorCollector>()
    fun createChild(nane: String): ErrorCollector {
        return ErrorCollector(file, nane, this)
            .also { children.add(it) }
    }

    private val errors = mutableListOf<Notation>()
    private val warnings = mutableListOf<Notation>()
    private val infos = mutableListOf<Notation>()

    fun getAllErrors(): List<Notation> = children.flatMap { it.getAllErrors() } + errors
    fun getAllWarnings(): List<Notation> = children.flatMap { it.getAllWarnings() } + warnings
    fun getAllInfos(): List<Notation> = children.flatMap { it.getAllInfos() } + infos

    fun getAllNotations() = (getAllErrors() + getAllWarnings() + getAllInfos()).sortedBy { it.range.start }

    fun getReport(
        includeErrors: Boolean = true,
        includeWarnings: Boolean = true,
        includeInfos: Boolean = true
    ): String? {
        if (hasNoMessages)
            return null

        val stringBuilder = StringBuilder()

        appendChildReport("", stringBuilder, includeErrors, includeWarnings, includeInfos)

        return stringBuilder.toString()
    }

    private fun appendChildReport(
        prefix: String,
        stringBuilder: StringBuilder,
        includeErrors: Boolean = true,
        includeWarnings: Boolean = true,
        includeInfos: Boolean = true
    ) {
        if (hasNoMessages)
            return

        stringBuilder.append("${prefix}Issues detected in $name:\n")

        val notations =
            (if (includeErrors) errors else emptyList()) +
                    (if (includeWarnings) warnings else emptyList()) +
                    (if (includeInfos) infos else emptyList())

        notations.sortedBy { it.range.start }.forEach {
            stringBuilder.append("$prefix    $it\n")
        }


        children.forEach {
            it.appendChildReport("$prefix    ", stringBuilder)
        }
    }

    val hasNoIssues: Boolean get() = hasNoErrors && hasNoWarnings
    val hasNoMessages: Boolean get() = hasNoErrors && hasNoWarnings && hasNoInfos

    val hasNoErrors: Boolean get() = getAllErrors().isEmpty()
    val hasNoWarnings: Boolean get() = getAllWarnings().isEmpty()
    val hasNoInfos: Boolean get() = getAllInfos().isEmpty()
    val hasErrors: Boolean get() = !hasNoErrors
    val hasWarnings: Boolean get() = !hasNoWarnings
    val hasInfos: Boolean get() = !hasNoInfos

    /**
     * Checks that this [ErrorCollector] has no errors or warnings.
     * If it has errors, it throws an [AssertionError] with a report as the reason.
     */
    fun assertNoIssues() {
        assert(hasNoIssues) { "\n" + (getReport(includeInfos = false) ?: "No issues in $name") }
    }

    /**
     * Checks that this [ErrorCollector] has no errors or warnings.
     * If it has errors, it throws an [AssertionError] with errors listed as the reason.
     */
    fun assertNoErrors() {
        assert(hasNoIssues) {
            "\n" + (getReport(includeWarnings = false, includeInfos = false) ?: "No errors in $name")
        }
    }

    fun printReport(
        includeErrors: Boolean = true,
        includeWarnings: Boolean = true,
        includeInfos: Boolean = true
    ) {
        val errorReport = getReport(includeErrors, includeWarnings, includeInfos)
        if (errorReport == null)
            println("Nothing to print for: $name")
        else
            println(errorReport)
    }

    fun clear() {
        children.forEach {
            it.clear()
        }
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