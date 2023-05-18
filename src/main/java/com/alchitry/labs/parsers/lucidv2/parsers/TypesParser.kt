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
    val dffs = mutableMapOf<String, Dff>()
    val sigs = mutableMapOf<String, Signal>()
    val moduleInstances = mutableMapOf<String, ModuleInstanceOrArray>()
    private val arraySizes = mutableMapOf<ArraySizeContext, Int>()
    private val assignmentBlocks = mutableListOf<AssignmentBlock>()

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        sigs[name]?.let { return it }
        moduleInstances[name]?.let { return it }

        return null
    }

    fun getInstances(): List<ModuleInstance> {
        return moduleInstances.values.flatMap { instOrArray ->
            when (instOrArray) {
                is ModuleInstance -> listOf(instOrArray)
                is ModuleInstanceArray -> instOrArray.getAllInstances()
            }
        }
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

        localSignalConnections.forEach { local ->
            if (extSignalConnections.any { it.port == local.port }) {
                context.reportError(local.portCtx, "The port \"${local.port}\" has already been assigned!")
                return
            }
        }

        localParamConnections.forEach { local ->
            if (extParamConnections.any { it.port == local.port }) {
                context.reportError(local.portCtx, "The port \"${local.port}\" has already been assigned!")
                return
            }
        }

        val providedSignals = localSignalConnections.union(extSignalConnections).map { it.port }
        val moduleSignals = moduleType.ports.filter { it.value.direction != SignalDirection.Write }.map { it.key }
        val requiredSignals = moduleType.ports.filter { it.value.direction == SignalDirection.Both }
            .map { it.key } // inouts must be assigned

        val extraSignals = providedSignals.filter { !moduleSignals.contains(it) }
        val missingSignals = requiredSignals.filter { !providedSignals.contains(it) }

        if (missingSignals.isNotEmpty()) {
            context.reportError(ctx, "Missing required signals: ${missingSignals.joinToString(", ")}")
            return
        }

        if (extraSignals.isNotEmpty()) {
            context.reportError(
                ctx,
                "The module \"${moduleTypeName}\" doesn't have these provided ports: ${extraSignals.joinToString(", ")}"
            )
        }

        val providedParams = localParamConnections.union(extParamConnections).map { it.port }
        val moduleParams = moduleType.parameters.values.map { it.name }
        val requiredParams = moduleType.parameters.values.mapNotNull { v -> if (v.default == null) v.name else null }

        val extraParams = providedParams.filter { !moduleParams.contains(it) }
        val missingParams = requiredParams.filter { !providedParams.contains(it) }

        if (missingParams.isNotEmpty()) {
            context.reportError(ctx, "Missing required parameters: ${missingParams.joinToString(", ")}")
            return
        }

        if (extraParams.isNotEmpty()) {
            context.reportError(
                ctx,
                "The module \"${moduleTypeName}\" doesn't have these provided parameters: ${missingParams.joinToString(", ")}"
            )
            return
        }

        extParamConnections.forEach { param ->
            if (!param.value.width.isFlat()) {
                context.reportError(param.portCtx, "Parameter \"${param.port}\" is not a simple value.")
                return
            }
        }

        fun getSignals(list: List<Connection>): Map<Connection, Signal>? {
            return list.associate { connection ->
                val port = moduleType.ports[connection.port] ?: error("Missing expected port!")
                if (port.direction == SignalDirection.Both) {
                    val exprCtx = connection.value.expr
                    // TODO: Support arrayed signals for arrayed module instances
                    if (exprCtx !is ExprSignalContext) {
                        context.reportError(exprCtx, "Inout ports must be directly connected to another inout.")
                        return null
                    }
                    val otherSig = context.resolve(exprCtx.signal())
                    if (otherSig !is Signal || otherSig.direction != SignalDirection.Both || otherSig.parent !is ModuleInstance) {
                        context.reportError(exprCtx, "Inout ports must be directly connected to another inout.")
                        return null
                    }
                    connection to otherSig
                } else {
                    connection to connection.value.asSignal(connection.value.expr.text)
                }
            }
        }

        val localSignals = getSignals(localSignalConnections) ?: return
        val extSignals = getSignals(extSignalConnections) ?: return

        val basicConnections = if (ctx.arraySize().isEmpty()) extSignals + localSignals else extSignals

        basicConnections.forEach { (connection, signal) ->
            val port = moduleType.ports[connection.port] ?: error("Missing expected port!")
            if (!port.width.canAssign(signal.width)) {
                context.reportError(
                    connection.portCtx,
                    "The width of \"${signal.name}\" does not match the width of port \"${port.name}\"."
                )
                return
            }
        }

        val instance = if (ctx.arraySize().isEmpty()) {
            localParamConnections.forEach { param ->
                if (!param.value.width.isFlat()) {
                    context.reportError(param.portCtx, "Parameter \"${param.port}\" is not a simple value.")
                    return
                }
            }

            val instParams = localParamConnections.union(extParamConnections).associate { it.port to it.value.value }
            val instPorts = localSignals.values.union(extSignals.values).associateBy { it.name }
            ModuleInstance(
                moduleInstanceName,
                context.project,
                context.instance,
                moduleType,
                instParams,
                instPorts
            ).apply {
                checkParameters()?.let {
                    this@TypesParser.context.reportError(ctx, it)
                    return
                }
                initialWalk()?.let {
                    this@TypesParser.context.reportError(ctx, it)
                    return
                }
            }
        } else {
            val dimensions = ctx.arraySize().map { arraySizes[it] ?: return }

            localSignals.forEach { (connection, signal) ->
                val port = moduleType.ports[connection.port] ?: error("Missing expected port!")

                var width = signal.width
                dimensions.forEach {
                    val curWidth = width
                    if (curWidth !is ArrayWidth || curWidth.size != it) {
                        context.reportError(
                            connection.portCtx,
                            "The signal \"${signal.name}\" does not match the dimensions of this module instance."
                        )
                        return
                    }
                    width = curWidth.next
                }

                if (!port.width.canAssign(width)) {
                    context.reportError(
                        connection.portCtx,
                        "The width of \"${signal.name}\" does not match the width of port \"${port.name}\"."
                    )
                    return
                }
            }

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

            // check for inouts in external connections
            // this isn't allowed since it would require connecting the inout to multiple module instances
            extSignals.forEach { (connection, _) ->
                val port = moduleType.ports[connection.port] ?: error("Missing expected port!")
                if (port.direction == SignalDirection.Both) {
                    context.reportError(
                        connection.portCtx,
                        "Inouts cannot be assigned by connection blocks to arrayed module instances!"
                    )
                    return
                }
            }

            ModuleInstanceArray(
                moduleInstanceName,
                context.project,
                context.instance,
                moduleType,
                dimensions,
                signalProvider = { idx ->
                    val selection = idx.map { SignalSelector.Bits(it) }
                    val localMap = localSignals.map { (connection, signal) ->
                        connection.port to signal.select(selection)
                    }.toMap()
                    val extMap = extSignals.map { (connection, signal) -> connection.port to signal }.toMap()
                    localMap + extMap
                },
                paramProvider = { idx ->
                    val selection = idx.map { SignalSelector.Bits(it) }
                    val localMap = localParamConnections.associate {
                        it.port to it.value.value.select(selection)
                    }
                    val extMap = extParamConnections.associate { it.port to it.value.value }
                    localMap + extMap
                }
            )
                .apply {
                    checkAllParameters()?.let {
                        this@TypesParser.context.reportError(ctx, it)
                        return
                    }
                    initialWalkAll()?.let {
                        this@TypesParser.context.reportError(ctx, it)
                        return
                    }
                }
        }

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
                        "The port \"$name\" has already been assigned!"
                    )
                else
                    signals.add(Connection(sigCtx.name(), DynamicExpr(sigCtx.expr(), context)))
            }
            connectionContext.paramCon()?.let { paramCtx ->
                val name = paramCtx.name().text
                if (assignmentBlocks.any { block -> block.params.any { it.port == name } })
                    context.reportError(
                        paramCtx.name(),
                        "The parameter \"$name\" has already been assigned!"
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
                                    "The initialization value is wider than the DFF \"$name\" and will be truncated."
                                )
                            }
                            init = it.resizeToMatch(init.width)
                        } else {
                            context.reportError(
                                param.value.expr,
                                "The initialization value does not match the size of the DFF $name."
                            )
                        }
                    }

                    else -> context.reportError(
                        param.value.expr,
                        "DFFs don't have a parameter named \"$paramName\"."
                    )
                }
            } else {
                context.reportError(
                    param.value.expr,
                    "The parameter \"$paramName\" has already been specified."
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
                        "DFFs don't have a signal named \"$sigName\"."
                    )
                }
            } else {
                context.reportError(
                    sig.portCtx,
                    "The signal \"$sigName\" has already been specified."
                )
            }

        }

        val resolvedClk = clk

        if (resolvedClk == null) {
            context.reportError(ctx, "Dff is missing connection to clk.")
            return
        }

        val clkWidth = resolvedClk.value.width
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

        rst?.value?.width?.let { rstWidth ->
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