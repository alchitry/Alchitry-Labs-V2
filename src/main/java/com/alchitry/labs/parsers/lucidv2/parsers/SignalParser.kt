package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.parsers.lucidv2.resolvers.StructResolver
import com.alchitry.labs.parsers.lucidv2.signals.Dff
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.values.*

class SignalParser(
    private val errorListener: ErrorListener = dummyErrorListener,
    private val resolver: LucidParseContext
) : LucidBaseListener(), SignalResolver, StructResolver {
    private val dffs = mutableMapOf<String, Dff>()

    private val structTypes = mutableMapOf<String, StructType>()
    private val resolvedStructTypes = mutableMapOf<StructTypeContext, StructType>()

    private val signalConnectionBlocks = mutableListOf<MutableMap<String, ExprContext>>()

    private val arraySizes = mutableMapOf<ArraySizeContext, Int>()

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        // TODO: Add other names

        return null
    }

    fun resolveStructType(name: String): StructType? = structTypes[name]
    override fun resolve(ctx: StructTypeContext): StructType? = resolvedStructTypes[ctx]

    override fun exitStructType(ctx: StructTypeContext) {
        val name = ctx.name().firstOrNull()?.text ?: return

        // TODO: Support global structs
        if (ctx.name().size > 1) {
            resolver.errorCollector.reportError(ctx.name().first(), "Global structs aren't supported yet...")
            return
        }

        val type = structTypes[name]

        if (type == null) {
            resolver.errorCollector.reportError(ctx.name(0), "Failed to find struct with name $name.")
            return
        }

        resolvedStructTypes[ctx] = type
    }

    override fun exitArraySize(ctx: ArraySizeContext) {
        val expr = resolver.expr.resolve(ctx.expr())
        if (expr == null) {
            resolver.errorCollector.reportError(ctx, "Array size expression couldn't be resolved.")
            return
        }

        if (!expr.constant)
            resolver.errorCollector.reportError(ctx, "Array sizes must be a constant value.")

        if (expr !is BitListValue || !expr.isNumber()) {
            resolver.errorCollector.reportError(ctx, "Array sizes must be a number.")
            return
        }

        val size = try {
            expr.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            resolver.errorCollector.reportError(ctx, "Array size must fit into an integer.")
            return
        }

        arraySizes[ctx] = size
    }

    override fun exitStructDec(ctx: StructDecContext) {
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            resolver.errorCollector.reportError(ctx.name(), "The struct name $name must start with a lowercase letter.")

        val members = mutableMapOf<String, SignalWidth>()

        ctx.structMember().forEach { structMemberContext ->
            val memberName = structMemberContext.name().text

            if (structMemberContext.name().TYPE_ID() == null)
                resolver.errorCollector.reportError(
                    structMemberContext.name(),
                    "The struct member name $memberName must start with a lowercase letter."
                )

            val subStructType = structMemberContext.structType()?.let { resolvedStructTypes[it] }
            val arraySizes = structMemberContext.arraySize().mapNotNull { arraySizes[it] }

            var width = subStructType?.let { StructWidth(it) }
                ?: arraySizes.lastOrNull()?.let { SimpleWidth(it) }
                ?: SimpleWidth(1)

            val lastIdx = arraySizes.size - if (subStructType != null) 0 else 1

            if (lastIdx >= 0)
                arraySizes.subList(0, lastIdx).asReversed().forEach {
                    width = ArrayWidth(it, width)
                }

            members[memberName] = width
        }

        structTypes[name] = StructType(name, members)
    }

    private fun filledArrayValue(bit: Bit, dims: List<Int>, signed: Boolean): Value {
        if (dims.isEmpty()) return bit.toBitValue(true, signed)

        val base = BitListValue(dims[0], true, signed) { bit }

        if (dims.size == 1) return base

        var lastValue: Value = base

        repeat(dims.size - 1) {
            lastValue = ArrayValue(List(dims[it + 1]) { lastValue })
        }

        return lastValue
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val signed = ctx.SIGNED() != null
        val structType = ctx.structType()?.let { resolvedStructTypes[it] } // TODO: use struct

        ctx.dffSingle().forEach { dffCtx ->
            val name = dffCtx.name().text

            if (dffCtx.name().TYPE_ID() == null)
                resolver.errorCollector.reportError(dffCtx.name(), "Dff names must start with a lowercase letter.")

            val dims = dffCtx.arraySize()
                .asReversed()
                .mapNotNull { (resolver.expr.resolve(it.expr()) as? BitListValue)?.toBigInt()?.intValueExact() }

            var clkCtx: ExprContext? = null
            var rstCtx: ExprContext? = null
            var init: Value = filledArrayValue(Bit.B0, dims, signed)

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
                                    if (init is BitListValue && it is BitListValue && (init as BitListValue).size < it.size) {
                                        errorListener.reportWarning(
                                            param,
                                            "The initialization value is wider than the DFF and will be truncated."
                                        )
                                    }
                                    init = it.resizeToMatch(init.signalWidth)

                                } else {
                                    errorListener.reportError(
                                        param,
                                        "The initialization value does not match the size of the DFF."
                                    )
                                }
                            }
                        }
                    } else {
                        errorListener.reportError(param, "The parameter $paramName has already been specified.")
                    }

                } else if (sig != null) {
                    val sigName = sig.name().text
                    if (connectedSignals.add(sigName)) {
                        when (sig.name().text) {
                            "clk" -> clkCtx = sig.expr()
                            "rst" -> rstCtx = sig.expr()
                        }
                    } else {
                        errorListener.reportError(sig, "The signal $sigName has already been specified.")
                    }

                }
            }

            // TODO: Check for external connections (assignment blocks)

            val resolvedClkCtx = clkCtx


            if (resolvedClkCtx == null) {
                errorListener.reportError(dffCtx, "Dff is missing connection to clk.")
                return
            }
            val clk = DynamicExpr(resolvedClkCtx, resolver)
            val rst = rstCtx?.let { DynamicExpr(it, resolver) }
            val dff = Dff(name, init, clk, rst)
            dffs[name] = dff
        }
    }
}