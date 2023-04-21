package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.values.DynamicExpr
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.parsers.lucidv2.signals.Dff
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.values.*
import org.antlr.v4.runtime.tree.ParseTree

class SignalParser(
    private val errorListener: ErrorListener = dummyErrorListener,
    private val resolver: LucidParseContext
) : LucidBaseListener(), SignalResolver {
    private val dffs = mutableMapOf<String, Dff>()

    private val structTypes = mutableMapOf<String, StructType>()
    private val resolvedStructTypes = mutableMapOf<ParseTree, StructType>()

    private val signalConnectionBlocks = mutableListOf<MutableMap<String, ExprContext>>()

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }

        return null
    }

    private fun filledArrayValue(bitValue: BitValue, dims: List<Int>, signed: Boolean): Value {
        if (dims.isEmpty()) return bitValue.toSimpleValue(true, signed)

        val base = MutableBitList(signed).also { list ->
            repeat(dims[0]) { list.add(bitValue) }
        }.toSimpleValue(true)

        if (dims.size == 1) return base

        var lastValue: Value = base

        repeat(dims.size - 1) {
            lastValue = ArrayValue(List(dims[it + 1]) { lastValue })
        }

        return lastValue
    }

    override fun exitDffDec(ctx: LucidParser.DffDecContext) {
        val signed = ctx.SIGNED() != null
        val structType = ctx.structType()?.let { resolvedStructTypes[it] } // TODO: use struct

        ctx.dffSingle().forEach { dffCtx ->
            val name = dffCtx.name().text
            val dims = dffCtx.arraySize()
                .asReversed()
                .mapNotNull { (resolver.expr.resolve(it.expr()) as? SimpleValue)?.toBigInt()?.intValueExact() }

            var clkCtx: ExprContext? = null
            var rstCtx: ExprContext? = null
            var init: Value = filledArrayValue(BitValue.B0, dims, signed)

            val connectedSignals = mutableSetOf<String>()
            val connectedParams = mutableSetOf<String>()

            dffCtx.instCons()?.conList()?.connection()?.forEach { connection ->
                val param = connection.paramCon()
                val sig = connection.sigCon()
                if (param != null) {
                    val paramName = param.name().text
                    if (connectedParams.add(paramName)) {
                        when (paramName) {
                            "INIT" -> resolver.expr.resolve(param.expr())?.let {
                                if (init.canAssign(it)) {
                                    if (init is SimpleValue && it is SimpleValue && (init as SimpleValue).size < it.size) {
                                        errorListener.reportWarning(
                                            param,
                                            "The initialization value is wider than the DFF and will be truncated"
                                        )
                                    }
                                    init = it.resizeToMatch(init)

                                } else {
                                    errorListener.reportError(
                                        param,
                                        "The initialization value does not match the size of the DFF"
                                    )
                                }
                            }
                        }
                    } else {
                        errorListener.reportError(param, "The parameter $paramName has already been specified")
                    }

                } else if (sig != null) {
                    val sigName = sig.name().text
                    if (connectedSignals.add(sigName)) {
                        when (sig.name().text) {
                            "clk" -> clkCtx = sig.expr()
                            "rst" -> rstCtx = sig.expr()
                        }
                    } else {
                        errorListener.reportError(sig, "The signal $sigName has already been specified")
                    }

                }
            }

            // TODO: Check for external connections (assignment blocks)

            val resolvedClkCtx = clkCtx


            if (resolvedClkCtx == null) {
                errorListener.reportError(dffCtx, "Dff is missing connection to clk")
                return
            }
            val clk = DynamicExpr(resolvedClkCtx, resolver)
            val rst = rstCtx?.let{ DynamicExpr(it, resolver) }
            val dff = Dff(name, init, clk, rst)
            dffs[name] = dff
        }
    }
}