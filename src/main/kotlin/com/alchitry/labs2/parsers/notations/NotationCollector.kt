package com.alchitry.labs2.parsers.notations

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.alchitry.kotlinmultiplatform.BitSet
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.collections.immutable.toImmutableMap
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
class NotationCollector private constructor(
    val file: ProjectFile,
    val name: String,
    private val parent: NotationCollector?
) : ErrorListener,
    ANTLRErrorListener {
    constructor(file: ProjectFile) : this(file, file.name, null)

    private val children = mutableListOf<NotationCollector>()
    fun createChild(nane: String): NotationCollector {
        return NotationCollector(file, nane, this)
            .also { children.add(it) }
    }

    private val errors = mutableSetOf<Notation>()
    private val warnings = mutableSetOf<Notation>()
    private val infos = mutableSetOf<Notation>()
    private val lineActions = mutableMapOf<Int, MutableList<LineAction>>()

    fun getAllErrors(): List<Notation> = (children.flatMap { it.getAllErrors() } + errors).distinct()
    fun getAllWarnings(): List<Notation> = (children.flatMap { it.getAllWarnings() } + warnings).distinct()
    fun getAllInfos(): List<Notation> = (children.flatMap { it.getAllInfos() } + infos).distinct()

    fun getAllNotations() = (getAllErrors() + getAllWarnings() + getAllInfos()).sortedBy { it.range.start }

    fun getLineActions() = lineActions.toImmutableMap()

    fun addLineAction(line: Int, content: @Composable BoxScope.() -> Unit) {
        lineActions.getOrPut(line) { mutableListOf() }.add(LineAction(line, content))
    }

    fun getReport(
        includeErrors: Boolean = true,
        includeWarnings: Boolean = true,
        includeInfos: Boolean = true
    ): AnnotatedString? {
        return AnnotatedString.Builder().apply {
            if (hasNoMessages)
                return null

            appendChildReport("", includeErrors, includeWarnings, includeInfos)
        }.toAnnotatedString()
    }

    context(AnnotatedString.Builder)
    private fun appendChildReport(
        prefix: String,
        includeErrors: Boolean = true,
        includeWarnings: Boolean = true,
        includeInfos: Boolean = true
    ) {
        if (hasNoMessages)
            return

        appendLine("${prefix}Issues detected in $name:")

        val notations =
            (if (includeErrors) errors else emptyList()) +
                    (if (includeWarnings) warnings else emptyList()) +
                    (if (includeInfos) infos else emptyList())

        notations.sortedBy { it.range.start }.forEach {
            val style = when (it.type) {
                NotationType.Error -> SpanStyle(color = AlchitryColors.current.Error)
                NotationType.Warning -> SpanStyle(color = AlchitryColors.current.Warning)
                NotationType.Info -> SpanStyle(color = AlchitryColors.current.Info)
                NotationType.SyntaxError -> SpanStyle(color = AlchitryColors.current.Error)
                NotationType.SyntaxAmbiguity -> SpanStyle(color = AlchitryColors.current.Warning)
            }
            withStyle(style) {
                append("$prefix    $it\n")
            }
        }

        children.forEach {
            it.appendChildReport("$prefix    ")
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
     * Checks that this [NotationCollector] has no errors or warnings.
     * If it has errors, it throws an [AssertionError] with a report as the reason.
     */
    fun assertNoIssues() {
        assert(hasNoIssues) { "\n" + (getReport(includeInfos = false) ?: "No issues in $name") }
    }

    /**
     * Checks that this [NotationCollector] has no errors or warnings.
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
        lineActions.clear()
    }

    override fun reportError(node: TerminalNode, message: String) {
        node.symbol?.let {
            errors.add(Notation.from(it, message, NotationType.Error))
        }
    }

    override fun reportError(ctx: ParserRuleContext, message: String) {
        Notation.from(ctx, message, NotationType.Error)?.let { errors.add(it) }
    }

    override fun reportWarning(node: TerminalNode, message: String) {
        node.symbol?.let {
            warnings.add(Notation.from(it, message, NotationType.Warning))
        }
    }

    override fun reportWarning(ctx: ParserRuleContext, message: String) {
        Notation.from(ctx, message, NotationType.Warning)?.let { warnings.add(it) }
    }

    override fun reportInfo(node: TerminalNode, message: String) {
        node.symbol?.let {
            infos.add(Notation.from(it, message, NotationType.Info))
        }
    }

    override fun reportInfo(ctx: ParserRuleContext, message: String) {
        Notation.from(ctx, message, NotationType.Info)?.let { infos.add(it) }
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
        errors.add(Notation.from(token, msg, NotationType.SyntaxError))
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
        errors.add(Notation.from(token, null, NotationType.SyntaxAmbiguity))
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