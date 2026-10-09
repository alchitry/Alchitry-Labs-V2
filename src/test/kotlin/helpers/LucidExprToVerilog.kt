package helpers

import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.hdl.lucid.SystemVerilogConverter
import com.alchitry.labs2.parsers.hdl.lucid.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.lucid.parsers.ParseStage
import com.alchitry.labs2.parsers.hdl.types.Signal
import kotlinx.coroutines.runBlocking

/**
 * Converts a Lucid expression string into its equivalent SystemVerilog expression string.
 *
 * @param expr The Lucid expression as a string.
 * @param localSignalResolver Optional [SignalResolver] for resolving signals referenced in the expression.
 * @return The converted SystemVerilog expression string.
 */
suspend fun convertLucidExprToVerilogSuspend(
    expr: String,
    localSignalResolver: SignalResolver? = null
): String {
    val tester = SimpleLucidTester(expr, localSignalResolver = localSignalResolver)
    val exprCtx = tester.parser.expr()

    if (tester.parser.tokenStream.LA(1) != com.alchitry.labs2.parsers.grammar.LucidParser.Tokens.EOF) {
        error("Failed to parse Lucid expression \"$expr\": unparsed trailing tokens remaining")
    }

    if (tester.context.notationCollector.hasErrors) {
        val errors = tester.context.notationCollector.getAllErrors().joinToString("\n") { it.message ?: it.toString() }
        error("Failed to parse Lucid expression \"$expr\":\n$errors")
    }

    // Walk with default ErrorCheck stage to resolve types, constants, signals, and expressions
    tester.context.walk(exprCtx)

    if (tester.context.notationCollector.hasErrors) {
        val errors = tester.context.notationCollector.getAllErrors().joinToString("\n") { it.message ?: it.toString() }
        error("Errors while resolving Lucid expression \"$expr\":\n$errors")
    }

    // Convert to SystemVerilog
    val converter = SystemVerilogConverter(tester.context)
    ParseTreeMultiWalker.walk(
        listOf(tester.context.expr, tester.context.signal, converter),
        exprCtx,
        ParseStage.Convert.filter,
        ignoreSkip = true
    )

    return converter.verilog[exprCtx]
        ?: error("Failed to generate Verilog for Lucid expression \"$expr\"")
}

/**
 * Converts a Lucid expression string into its equivalent SystemVerilog expression string.
 *
 * @param expr The Lucid expression as a string.
 * @param localSignalResolver Optional [SignalResolver] for resolving signals referenced in the expression.
 * @return The converted SystemVerilog expression string.
 */
fun convertLucidExprToVerilog(
    expr: String,
    localSignalResolver: SignalResolver? = null
): String = runBlocking {
    convertLucidExprToVerilogSuspend(expr, localSignalResolver)
}

/**
 * Converts a Lucid expression string into its equivalent SystemVerilog expression string using provided [signals].
 */
fun convertLucidExprToVerilog(
    expr: String,
    vararg signals: Signal
): String = convertLucidExprToVerilog(
    expr,
    if (signals.isEmpty()) null else TestSignalResolver(*signals)
)

/** Alias for [convertLucidExprToVerilog]. */
fun lucidExprToVerilog(
    expr: String,
    localSignalResolver: SignalResolver? = null
): String = convertLucidExprToVerilog(expr, localSignalResolver)

/** Alias for [convertLucidExprToVerilog]. */
fun lucidExprToVerilog(
    expr: String,
    vararg signals: Signal
): String = convertLucidExprToVerilog(expr, *signals)