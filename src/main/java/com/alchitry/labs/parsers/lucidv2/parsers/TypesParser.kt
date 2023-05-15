package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.types.Dff
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstanceArray
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstanceOrArray
import com.alchitry.labs.parsers.lucidv2.values.*

data class TypesParser(
    private val context: LucidModuleContext
) : LucidBaseListener(), SignalResolver {
    private val dffs = mutableMapOf<String, Dff>()
    private val sigs = mutableMapOf<String, Signal>()
    private val moduleInstances = mutableMapOf<String, ModuleInstanceOrArray>()
    private val arraySizes = mutableMapOf<ArraySizeContext, Int>()
    private val assignmentBlocks = mutableListOf<AssignmentBlock>()

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        sigs[name]?.let { return it }
        moduleInstances[name]?.let { return it }

        return null
    }

    override fun exitModuleInst(ctx: ModuleInstContext) {
        val moduleTypeName = ctx.name(0)?.text ?: return
        val moduleInstanceName = ctx.name(1)?.text ?: return

        val moduleType = context.project.resolveModuleType(moduleTypeName)

        if (moduleType == null) {
            context.reportError(ctx.name(0), "Module with name $moduleTypeName could not be resolved.")
            return
        }

        if (ctx.name(1).TYPE_ID() == null) {
            context.reportError(ctx.name(1), "Module instance names must start with a lowercase letter.")
            return
        }

        if (context.resolveSignal(moduleInstanceName) != null) {
            context.reportError(ctx.name(1), "The name $moduleInstanceName is already in use.")
            return
        }

        val localSignalConnections = mutableListOf<Connection>()
        val localParamConnections = mutableListOf<Connection>()

        val extSignalConnections = mutableListOf<Connection>()
        val extParamConnections = mutableListOf<Connection>()

        ctx.instCons()?.connection()?.forEach { connection ->
            connection.sigCon()?.let { sigCtx ->
                localSignalConnections.add(Connection(sigCtx.name(), DynamicExpr(sigCtx.expr(), context)))
            }
            connection.paramCon()?.let { paramCtx ->
                localParamConnections.add(Connection(paramCtx.name(), DynamicExpr(paramCtx.expr(), context)))
            }
        }

        assignmentBlocks.forEach { block ->
            extSignalConnections.addAll(block.signals)
            extParamConnections.addAll(block.params)
        }

        val providedParams = localParamConnections.union(extParamConnections).map { it.port }
        val moduleParams = moduleType.parameters.values.map { it.name }
        val requiredParams = moduleType.parameters.values.mapNotNull { v -> v.default?.let { v.name } }

        val extraParams = providedParams.filter { !moduleParams.contains(it) }
        val missingParams = requiredParams.filter { !providedParams.contains(it) }

        if (missingParams.isNotEmpty()) {
            context.reportError(ctx, "Missing required parameters: ${missingParams.joinToString(", ")}")
            return
        }

        if (extraParams.isNotEmpty()) {
            context.reportError(
                ctx,
                "This module doesn't have these provided parameters: ${missingParams.joinToString(", ")}"
            )
            return
        }

        extParamConnections.forEach { param ->
            if (!param.value.width.isFlat()) {
                context.reportError(param.portCtx, "Parameter ${param.port} is not a simple value.")
                return
            }
        }

        val instance = if (ctx.arraySize().isEmpty()) {
            localParamConnections.forEach { param ->
                if (!param.value.width.isFlat()) {
                    context.reportError(param.portCtx, "Parameter ${param.port} is not a simple value.")
                    return
                }
            }

            val instParams = localParamConnections.union(extParamConnections).associate { it.port to it.value.value }
            ModuleInstance(moduleInstanceName, context.project, context.instance, moduleType, instParams).apply {
                checkParameters()?.let {
                    context.reportError(ctx, it)
                    return
                }
                initialWalk()?.let {
                    context.reportError(ctx, it)
                    return
                }
            }
        } else {
            val dimensions = ctx.arraySize().map { arraySizes[it] ?: return }

            localParamConnections.forEach { param ->
                var width = param.value.width
                dimensions.forEach {
                    val curWidth = width
                    if (curWidth !is ArrayWidth || curWidth.size != it) {
                        context.reportError(
                            param.portCtx,
                            "The parameter ${param.port} does not match the dimensions of this module instance."
                        )
                        return
                    }
                    width = curWidth.next
                }
                if (!width.isFlat()) {
                    context.reportError(param.portCtx, "Parameter ${param.port} does not index to a simple value.")
                    return
                }
            }

            ModuleInstanceArray(moduleInstanceName, context.project, context.instance, moduleType, dimensions) { idx ->
                val selection = idx.map { SignalSelector.Bits(it) }
                val localMap = localParamConnections.associate {
                    it.port to it.value.value.select(selection)
                }
                val extMap = extParamConnections.associate { it.port to it.value.value }
                localMap + extMap
            }.apply {
                checkAllParameters()?.let {
                    context.reportError(ctx, it)
                    return
                }
                initialWalkAll()?.let {
                    context.reportError(ctx, it)
                    return
                }
            }
        }

        // TODO: connect signals
        moduleInstances[instance.name] = instance
    }

    override fun exitConList(ctx: ConListContext) {
        val signals = mutableListOf<Connection>()
        val params = mutableListOf<Connection>()
        ctx.connection().forEach { connectionContext ->
            connectionContext.sigCon()?.let { sigCtx ->
                val name = sigCtx.name().text
                if (assignmentBlocks.any { block -> block.signals.any { it.port == name } })
                    context.reportError(
                        sigCtx.name(),
                        "The signal $name has already been assigned!"
                    )
                else
                    signals.add(Connection(sigCtx.name(), DynamicExpr(sigCtx.expr(), context)))
            }
            connectionContext.paramCon()?.let { paramCtx ->
                val name = paramCtx.name().text
                if (assignmentBlocks.any { block -> block.params.any { it.port == name } })
                    context.reportError(
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
            context.reportError(ctx, "Array size expression couldn't be resolved.")
            return
        }

        if (!expr.constant)
            context.reportError(ctx, "Array sizes must be a constant value.")

        if (expr !is BitListValue || !expr.isNumber()) {
            context.reportError(ctx, "Array sizes must be a number.")
            return
        }

        val size = try {
            expr.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.reportError(ctx, "Array size must fit into an integer.")
            return
        }

        arraySizes[ctx] = size
    }


    override fun exitSigDec(ctx: SigDecContext) {
        val signed = ctx.SIGNED() != null
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.reportError(ctx.name(), "Sig names must start with a lowercase letter.")

        val width = context.resolve(ctx.signalWidth())

        if (width == null) {
            context.reportError(ctx.signalWidth(), "Failed to resolve signal width!")
            return
        }

        sigs[name] = Signal(name, SignalDirection.Both, null, width.filledWith(Bit.Bx, false, signed), signed)
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val signed = ctx.SIGNED() != null

        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.reportError(ctx.name(), "Dff names must start with a lowercase letter.")

        var clk: DynamicExpr? = null
        var rst: DynamicExpr? = null
        val width = context.resolve(ctx.signalWidth())

        if (width == null) {
            context.reportError(ctx.signalWidth(), "Failed to resolve signal width!")
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
                                context.reportWarning(
                                    param.value.expr,
                                    "The initialization value is wider than the DFF $name and will be truncated."
                                )
                            }
                            init = it.resizeToMatch(init.signalWidth)
                        } else {
                            context.reportError(
                                param.value.expr,
                                "The initialization value does not match the size of the DFF $name."
                            )
                        }
                    }

                    else -> context.reportError(
                        param.value.expr,
                        "DFFs don't have a parameter named $paramName."
                    )
                }
            } else {
                context.reportError(
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
                    else -> context.reportError(
                        sig.value.expr,
                        "DFFs don't have a signal named $sigName."
                    )
                }
            } else {
                context.reportError(
                    sig.portCtx,
                    "The signal $sigName has already been specified."
                )
            }

        }

        val resolvedClk = clk

        if (resolvedClk == null) {
            context.reportError(ctx, "Dff is missing connection to clk.")
            return
        }

        val clkWidth = resolvedClk.value.signalWidth
        if (!BitWidth.canAssign(clkWidth)) {
            context.reportError(ctx, "The clk connection can't be reduced to a single bit.")
            return
        }

        if (clkWidth != BitWidth) {
            context.reportWarning(
                resolvedClk.expr,
                "The clk connection is wider than 1 bit and will be truncated."
            )
        }

        rst?.value?.signalWidth?.let { rstWidth ->
            if (!BitWidth.canAssign(rstWidth)) {
                context.reportError(ctx, "The rst connection can't be reduced to a single bit.")
                return
            }
            if (rstWidth != BitWidth) {
                context.reportWarning(
                    resolvedClk.expr,
                    "The rst connection is wider than 1 bit and will be truncated."
                )
            }
        }

        val dff = Dff(context.project, name, init, resolvedClk.constrain(BitWidth), rst?.constrain(BitWidth), signed)
        dffs[name] = dff
    }
}