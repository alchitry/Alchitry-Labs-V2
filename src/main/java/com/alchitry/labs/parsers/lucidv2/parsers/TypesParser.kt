package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.DynamicExpr
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.types.Dff
import com.alchitry.labs.parsers.lucidv2.values.*

data class TypesParser(
    private val context: LucidModuleContext,
    private val dffs: MutableMap<String, Dff> = mutableMapOf(),
    private val sigs: MutableMap<String, Signal> = mutableMapOf(),

    private val arraySizes: MutableMap<ArraySizeContext, Int> = mutableMapOf(),
    private val assignmentBlocks: MutableList<AssignmentBlock> = mutableListOf()
) : LucidBaseListener(), SignalResolver {
    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        sigs[name]?.let { return it }

        return null
    }

    override fun exitConList(ctx: ConListContext) {
        val signals = mutableListOf<Connection>()
        val params = mutableListOf<Connection>()
        ctx.connection().forEach { connectionContext ->
            connectionContext.sigCon()?.let { sigCtx ->
                val name = sigCtx.name().text
                if (assignmentBlocks.any { block -> block.signals.any { it.port == name } })
                    context.errorCollector.reportError(
                        sigCtx.name(),
                        "The signal $name has already been assigned!"
                    )
                else
                    signals.add(Connection(sigCtx.name(), DynamicExpr(sigCtx.expr(), context)))
            }
            connectionContext.paramCon()?.let { paramCtx ->
                val name = paramCtx.name().text
                if (assignmentBlocks.any { block -> block.params.any { it.port == name } })
                    context.errorCollector.reportError(
                        paramCtx.name(),
                        "The parameter $name has already been assigned!"
                    )
                else
                    params.add(Connection(paramCtx.name(), DynamicExpr(paramCtx.expr(), context)))
            }
        }
        assignmentBlocks.add(AssignmentBlock(signals, params))
    }

    override fun exitAssignBlock(ctx: AssignBlockContext) {
        assignmentBlocks.removeLast()
    }

    override fun exitArraySize(ctx: ArraySizeContext) {
        val expr = context.expr.resolve(ctx.expr())
        if (expr == null) {
            context.errorCollector.reportError(ctx, "Array size expression couldn't be resolved.")
            return
        }

        if (!expr.constant)
            context.errorCollector.reportError(ctx, "Array sizes must be a constant value.")

        if (expr !is BitListValue || !expr.isNumber()) {
            context.errorCollector.reportError(ctx, "Array sizes must be a number.")
            return
        }

        val size = try {
            expr.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportError(ctx, "Array size must fit into an integer.")
            return
        }

        arraySizes[ctx] = size
    }


    override fun exitSigDec(ctx: SigDecContext) {
        val signed = ctx.SIGNED() != null
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.errorCollector.reportError(ctx.name(), "Sig names must start with a lowercase letter.")

        val width = context.resolve(ctx.signalWidth())

        if (width == null) {
            context.errorCollector.reportError(ctx.signalWidth(), "Failed to resolve signal width!")
            return
        }

        sigs[name] = Signal(name, SignalDirection.Both, null, width.filledWith(Bit.Bx, false, signed), signed)
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val signed = ctx.SIGNED() != null

        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.errorCollector.reportError(ctx.name(), "Dff names must start with a lowercase letter.")

        var clk: DynamicExpr? = null
        var rst: DynamicExpr? = null
        val width = context.resolve(ctx.signalWidth())

        if (width == null) {
            context.errorCollector.reportError(ctx.signalWidth(), "Failed to resolve signal width!")
            return
        }

        var init: Value = width.filledWith(Bit.B0, true, signed)

        val connectedSignals = mutableSetOf<String>()
        val connectedParams = mutableSetOf<String>()

        val signalConnections = mutableListOf<Connection>()
        val paramConnections = mutableListOf<Connection>()

        ctx.instCons()?.connection()?.forEach { connection ->
            connection.sigCon()?.let { sigCtx ->
                signalConnections.add(Connection(sigCtx.name(), DynamicExpr(sigCtx.expr(), context)))
            }
            connection.paramCon()?.let { paramCtx ->
                paramConnections.add(Connection(paramCtx.name(), DynamicExpr(paramCtx.expr(), context)))
            }
        }

        assignmentBlocks.forEach { block ->
            signalConnections.addAll(block.signals)
            paramConnections.addAll(block.params)
        }

        paramConnections.forEach { param ->
            val paramName = param.port
            if (connectedParams.add(paramName)) {
                when (paramName) {
                    "INIT" -> param.value.value.let {
                        if (init.canAssign(it)) {
                            if (init is SimpleValue && it is SimpleValue && (init as SimpleValue).size < it.size) {
                                context.errorCollector.reportWarning(
                                    param.value.expr,
                                    "The initialization value is wider than the DFF $name and will be truncated."
                                )
                            }
                            init = it.resizeToMatch(init.signalWidth)
                        } else {
                            context.errorCollector.reportError(
                                param.value.expr,
                                "The initialization value does not match the size of the DFF $name."
                            )
                        }
                    }

                    else -> context.errorCollector.reportError(
                        param.value.expr,
                        "DFFs don't have a parameter named $paramName."
                    )
                }
            } else {
                context.errorCollector.reportError(
                    param.value.expr,
                    "The parameter $paramName has already been specified."
                )
            }

        }

        signalConnections.forEach { sig ->
            val sigName = sig.port
            if (connectedSignals.add(sigName)) {
                when (sigName) {
                    "clk" -> clk = sig.value // TODO: Check width fits to single bit
                    "rst" -> rst = sig.value
                    else -> context.errorCollector.reportError(
                        sig.value.expr,
                        "DFFs don't have a signal named $sigName."
                    )
                }
            } else {
                context.errorCollector.reportError(
                    sig.portCtx,
                    "The signal $sigName has already been specified."
                )
            }

        }

        val resolvedClk = clk

        if (resolvedClk == null) {
            context.errorCollector.reportError(ctx, "Dff is missing connection to clk.")
            return
        }

        val clkWidth = resolvedClk.value.signalWidth
        if (!BitWidth.canAssign(clkWidth)) {
            context.errorCollector.reportError(ctx, "The clk connection can't be reduced to a single bit.")
            return
        }

        if (clkWidth != BitWidth) {
            context.errorCollector.reportWarning(
                resolvedClk.expr,
                "The clk connection is wider than 1 bit and will be truncated."
            )
        }

        rst?.value?.signalWidth?.let { rstWidth ->
            if (!BitWidth.canAssign(rstWidth)) {
                context.errorCollector.reportError(ctx, "The rst connection can't be reduced to a single bit.")
                return
            }
            if (rstWidth != BitWidth) {
                context.errorCollector.reportWarning(
                    resolvedClk.expr,
                    "The rst connection is wider than 1 bit and will be truncated."
                )
            }
        }

        val dff = Dff(context.project, name, init, resolvedClk.constrain(BitWidth), rst?.constrain(BitWidth), signed)
        dffs[name] = dff
    }
}