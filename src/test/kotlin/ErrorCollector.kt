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
        syntaxIssues.add("Syntax ambiguity at $startIndex to $stopIndex")
    }

    override fun reportAttemptingFullContext(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        conflictingAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
        syntaxIssues.add("Syntax attempting full context at $startIndex to $stopIndex")
    }

    override fun reportContextSensitivity(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        prediction: Int,
        configs: ATNConfigSet?
    ) {
        syntaxIssues.add("Syntax context sensitivity at $startIndex to $stopIndex")
    }
}