package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.parsers.lucidv2.resolvers.StructResolver
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.*

class SignalParser(
    private val context: LucidParseContext
) : LucidBaseListener(), SignalResolver, StructResolver {
    private val dffs = mutableMapOf<String, Dff>()

    private val structTypes = mutableMapOf<String, StructType>()
    private val resolvedStructTypes = mutableMapOf<StructTypeContext, StructType>()

    private val signalConnectionBlocks = mutableListOf<MutableMap<String, ExprContext>>()

    private val arraySizes = mutableMapOf<ArraySizeContext, Int>()

    private val assignmentBlocks = mutableListOf<AssignmentBlock>()

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        // TODO: Add other names

        return null
    }

    fun resolveStructType(name: String): StructType? = structTypes[name]
    override fun resolve(ctx: StructTypeContext): StructType? = resolvedStructTypes[ctx]

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

    override fun exitStructType(ctx: StructTypeContext) {
        val name = ctx.name().firstOrNull()?.text ?: return

        // TODO: Support global structs
        if (ctx.name().size > 1) {
            context.errorCollector.reportError(ctx.name().first(), "Global structs aren't supported yet...")
            return
        }

        val type = structTypes[name]

        if (type == null) {
            context.errorCollector.reportError(ctx.name(0), "Failed to find struct with name $name.")
            return
        }

        resolvedStructTypes[ctx] = type
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

    override fun exitStructDec(ctx: StructDecContext) {
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.errorCollector.reportError(ctx.name(), "The struct name $name must start with a lowercase letter.")

        val members = mutableMapOf<String, StructMember>()

        ctx.structMember().forEach { structMemberContext ->
            val memberName = structMemberContext.name().text
            val signed = structMemberContext.SIGNED() != null

            if (structMemberContext.name().TYPE_ID() == null)
                context.errorCollector.reportError(
                    structMemberContext.name(),
                    "The struct member name $memberName must start with a lowercase letter."
                )

            val subStructType = structMemberContext.structType()?.let { resolvedStructTypes[it] }
            val arraySizes = structMemberContext.arraySize().mapNotNull { arraySizes[it] }

            var width = subStructType?.let { StructWidth(it) }
                ?: arraySizes.lastOrNull()?.let { BitListWidth(it) }
                ?: BitWidth

            val lastIdx = arraySizes.size - if (subStructType != null) 0 else 1

            if (lastIdx >= 0)
                arraySizes.subList(0, lastIdx).asReversed().forEach {
                    width = ArrayWidth(it, width)
                }

            members[memberName] = StructMember(memberName, width, signed)
        }

        structTypes[name] = StructType(name, members)
    }

    private fun buildSignalWidth(dims: List<Int>, structType: StructType?): SignalWidth {
        if (dims.isEmpty()) return structType?.let { StructWidth(it) } ?: BitWidth

        val base = structType?.let { StructWidth(it) } ?: BitListWidth(dims[0])

        if (dims.size == 1 && structType == null) return base

        var lastWidth: SignalWidth = base

        val offset = if (structType == null) 1 else 0

        repeat(dims.size - offset) {
            lastWidth = ArrayWidth(dims[it + 1], lastWidth)
        }

        return lastWidth
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val signed = ctx.SIGNED() != null
        val structType = ctx.structType()?.let {
            resolvedStructTypes[it].also { v ->
                if (v == null)
                    context.errorCollector.reportError(
                        ctx.structType(),
                        "Failed to resolve struct type ${it.text}!"
                    )
            }
        }

        ctx.dffSingle().forEach { dffCtx ->
            val name = dffCtx.name().text

            if (dffCtx.name().TYPE_ID() == null)
                context.errorCollector.reportError(dffCtx.name(), "Dff names must start with a lowercase letter.")

            val dims = dffCtx.arraySize()
                .asReversed()
                .mapNotNull { (context.expr.resolve(it.expr()) as? BitListValue)?.toBigInt()?.intValueExact() }

            var clk: DynamicExpr? = null
            var rst: DynamicExpr? = null
            val width = buildSignalWidth(dims, structType)
            var init: Value = width.getFilledValue(Bit.B0, true, signed)
            println("Width: $width")
            println("init: ${init.signalWidth}")

            val connectedSignals = mutableSetOf<String>()
            val connectedParams = mutableSetOf<String>()

            val signalConnections = mutableListOf<Connection>()
            val paramConnections = mutableListOf<Connection>()

            dffCtx.instCons()?.connection()?.forEach { connection ->
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
                        "clk" -> clk = sig.value
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
                context.errorCollector.reportError(dffCtx, "Dff is missing connection to clk.")
                return
            }
            val dff = Dff(name, init, resolvedClk, rst)
            dffs[name] = dff
        }
    }
}