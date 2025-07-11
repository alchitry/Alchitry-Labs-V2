package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.noNulls
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs2.parsers.hasParent
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.lucid.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.ports.Inout
import com.alchitry.labs2.parsers.hdl.values.*
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.parsers.parents
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree

fun String.toSourceFile(name: String = "alchitry_top.luc") = SourceFile(FileProvider.StringFile(name, contents = this))

sealed class ArraySize {
    data class Fixed(val size: Int) : ArraySize()
    data class Resolvable(val context: ExprContext) : ArraySize()
}

class TypesParser(
    private val context: LucidBlockContext
) : SuspendLucidBaseListener(), SignalResolver {
    val dffs = mutableMapOf<String, Dff>()
    val sigs = mutableMapOf<String, Signal>()
    val signalDynamicExprs = mutableMapOf<Signal, DynamicExpr>()
    val moduleInstances = mutableMapOf<String, ModuleInstanceOrArray>()
    val dynamicExprs = mutableMapOf<ExprContext, DynamicExpr>()
    private val arraySizes = mutableMapOf<ArraySizeContext, ArraySize>()
    private val assignmentBlocks = mutableListOf<AssignmentBlock>()

    private val scopeStack = mutableListOf<ParserRuleContext>()
    private val currentScope get() = scopeStack.last()

    val localSignals = mutableMapOf<ParserRuleContext, MutableMap<String, LocalSignal>>()

    fun resolveLocalSignals(ctx: ParseTree): List<Signal> {
        val parentList = ctx.parents
        val scopes = localSignals.filterKeys { parentList.contains(it) }.values
        return scopes.flatMap { scope -> scope.values.map { it.signal } }
    }

    fun resolveLocalSignal(ctx: ParseTree, name: String): Signal? {
        val parentList = ctx.parents
        val scopes = localSignals.filterKeys { parentList.contains(it) }.values
        return scopes.firstOrNull { it.contains(name) }?.get(name)?.signal
    }

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        sigs[name]?.let { return it }
        moduleInstances[name]?.let { return it }
        resolveLocalSignal(ctx, name)?.let { return it }

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

    override suspend fun enterBlock(ctx: BlockContext) {
        scopeStack.add(ctx)
    }

    override suspend fun exitBlock(ctx: BlockContext) {
        scopeStack.removeLast()
    }

    override suspend fun enterCaseBlock(ctx: CaseBlockContext) {
        scopeStack.add(ctx)
    }

    override suspend fun exitCaseBlock(ctx: CaseBlockContext) {
        scopeStack.removeLast()
    }

    private fun checkParameterWidth(
        ctx: ParserRuleContext,
        parameterName: String,
        defaultWidth: SignalWidth?,
        assignedWidth: SignalWidth
    ): Boolean {
        when {
            defaultWidth == null && assignedWidth !is SimpleWidth -> {
                context.reportError(
                    ctx,
                    "Parameter \"$parameterName\" without a default value must have a simple (1D) width."
                )
                return false
            }

            defaultWidth != null && !Parameter.widthsCompatible(defaultWidth, assignedWidth) -> {
                context.reportError(
                    ctx,
                    "Parameter \"$parameterName\" must have a width compatible with the default value's width of ${defaultWidth.prettyPrint()}."
                )
                return false
            }
        }
        return true
    }

    override suspend fun exitModuleInst(ctx: ModuleInstContext) {
        val nameCtx = ctx.name()
        val moduleTypeName = nameCtx.getOrNull(0)?.text ?: return
        val moduleInstanceName = nameCtx.getOrNull(1)?.text ?: return

        val moduleType = context.project.resolveModuleType(moduleTypeName)

        if (moduleType == null) {
            context.reportError(nameCtx[0], "Module with name $moduleTypeName could not be resolved.")
            return
        }

        val parentModules = mutableListOf<Module>()
        var currentModule = (context.instance as? ModuleInstance)
        while (currentModule != null) {
            parentModules.add(currentModule.module)
            currentModule = currentModule.parent
        }

        if (parentModules.contains(moduleType)) {
            context.reportError(nameCtx[0], "Instantiating \"${moduleTypeName}\" would cause an instantiation loop.")
            return
        }

        if (nameCtx[1].TYPE_ID() == null) {
            context.reportError(nameCtx[1], "Module instance names must start with a lowercase letter.")
            return
        }

        if (context.resolveSignal(ctx, moduleInstanceName) != null) {
            context.reportError(nameCtx[1], "The name \"$moduleInstanceName\" is already in use.")
            return
        }

        val localSignalConnections = mutableListOf<Connection>()
        val localParamConnections = mutableListOf<Connection>()

        val extSignalConnections = mutableListOf<Connection>()
        val extParamConnections = mutableListOf<Connection>()

        ctx.instCons()?.connection()?.forEach { connection ->
            connection.sigCon()?.let { sigCtx ->
                localSignalConnections.add(
                    Connection(
                        sigCtx.name() ?: return@let,
                        try {
                            DynamicExpr(sigCtx.expr() ?: return@let, context)
                        } catch (e: IllegalStateException) {
                            return@let
                        }
                    )
                )
            }
            connection.paramCon()?.let { paramCtx ->
                localParamConnections.add(
                    Connection(
                        paramCtx.name() ?: return@let,
                        try {
                            DynamicExpr(paramCtx.expr() ?: return@let, context)
                        } catch (e: IllegalStateException) {
                            return@let
                        }
                    )
                )
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
        val moduleSignals = moduleType.ports
        val requiredSignals =
            if (ctx.hasParent<TestBenchContext>()) emptyList()
            else
                moduleType.ports.filter { it.value.isInout }.map { it.key } // inouts must be assigned

        val extraSignals = providedSignals.filter { !moduleSignals.contains(it) }
        val missingSignals = requiredSignals.filter { !providedSignals.contains(it) }

        if (missingSignals.isNotEmpty()) {
            context.reportError(ctx, "Missing required signals: ${missingSignals.joinToString(", ")}")
            return
        }

        if (extraSignals.isNotEmpty()) {
            context.reportError(
                ctx.name(0) ?: ctx,
                "The module \"${moduleTypeName}\" doesn't have these provided ports: ${extraSignals.joinToString(", ")}"
            )
            return
        }

        val providedParams = localParamConnections.union(extParamConnections).map { it.port }
        val moduleParams = moduleType.parameters.values.map { it.name }
        val requiredParams =
            moduleType.parameters.values.mapNotNull { v -> if (v.default == null || v.defaultTestOnly) v.name else null }

        val extraParams = providedParams.filter { !moduleParams.contains(it) }
        val missingParams = requiredParams.filter { !providedParams.contains(it) }

        if (missingParams.isNotEmpty()) {
            context.reportError(ctx, "Missing required parameters: ${missingParams.joinToString(", ")}")
            return
        }

        if (extraParams.isNotEmpty()) {
            context.reportError(
                ctx,
                "The module \"${moduleTypeName}\" doesn't have these provided parameters: ${extraParams.joinToString(", ")}"
            )
            return
        }

        fun getSignals(list: List<Connection>): Map<Connection, Signal>? {
            return list.associate { connection ->
                val port = moduleType.ports[connection.port] ?: error("Missing expected port!")

                when (port.direction) {
                    SignalDirection.Write -> {
                        context.reportError(
                            connection.portCtx,
                            "Module output \"${port.name}\" can't' be directly connected. Use \"$moduleInstanceName.${port.name}\" instead."
                        )
                        return null
                    }

                    SignalDirection.Read -> connection to connection.value.asSignal(connection.value.expr.text)
                    SignalDirection.Both -> {
                        val exprCtx = connection.value.expr
                        // TODO: Support arrayed signals for arrayed module instances
                        if (exprCtx !is ExprSignalContext) {
                            context.reportError(exprCtx, "Inout ports must be directly connected to another inout.")
                            return null
                        }
                        val otherSig = context.resolve(exprCtx.signal() ?: error("Signal missing expression!"))
                        if (otherSig !is Signal || otherSig.direction != SignalDirection.Both || otherSig.parent !is ModuleInstance) {
                            context.reportError(exprCtx, "Inout ports must be directly connected to another inout.")
                            return null
                        }
                        ((otherSig.parent as ModuleInstance).ports[otherSig.name] as Inout).passthrough = true
                        connection to otherSig
                    }
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

        val paramCtx = localParamConnections.union(extParamConnections).associate {
            val width = it.value.value.width
            if (!width.isResolvable() && !width.isSimple()) {
                context.reportError(
                    it.value.expr,
                    "The width for parameter \"${it.port}\" could not be resolved. Consider using \$resize() to define the width of parameters used in expressions."
                )
            }
            it.port to ParamAssignment(it.value, localParamConnections.contains(it))
        }

        extParamConnections.forEach {
            checkParameterWidth(
                ctx.name(0) ?: ctx,
                it.port,
                moduleType.parameters[it.port]?.default?.width,
                it.value.value.width
            )
        }


        val instance = if (ctx.arraySize().isEmpty()) {
            localParamConnections.forEach {
                checkParameterWidth(
                    it.portCtx,
                    it.port,
                    moduleType.parameters[it.port]?.default?.width,
                    it.value.value.width
                )
            }
            val instParams = localParamConnections.union(extParamConnections).associate {
                it.port to it.value.value
            }
            val instPorts = (localSignals + extSignals).mapKeys {
                if (moduleType.ports[it.key.port]?.isInout == true) {
                    if (context.boundInouts.putIfAbsent(it.value.name, it.value) != null || it.value.hasDriver) {
                        context.reportError(it.key.portCtx, "Inout \"${it.value.name}\" has already been bound!")
                        return
                    }
                }
                it.key.port
            }
            try {
                ModuleInstance(
                    moduleInstanceName,
                    context.project,
                    context.instance as? ModuleInstance,
                    moduleType,
                    paramCtx,
                    instParams,
                    instPorts,
                    mode = ExprEvalMode.Default
                    // context.notationCollector.createChild("ModuleInstance($moduleInstanceName)")
                ).apply {
                    if (!this@TypesParser.context.mode.building) {
                        val errorCollector = NotationCollector(context.sourceFile)
                        checkParameters(errorCollector)
                        if (errorCollector.hasErrors) {
                            this@TypesParser.context.reportError(
                                ctx.name(1) ?: ctx,
                                errorCollector.getAllErrors().joinToString(", ") { it.message ?: "" }
                            )
                            return
                        }
                        initialWalk()
                        if (errorCollector.hasErrors) {
                            this@TypesParser.context.reportError(
                                ctx.name(1) ?: ctx,
                                errorCollector.getAllErrors().joinToString(", ") { it.message ?: "" }
                            )
                            return
                        }
                    }
                }
            } catch (e: ConnectionException) {
                val portContext = (localSignals + extSignals).keys.first { it.port == e.port }.portCtx
                context.reportError(portContext, e.message)
                return
            } catch (e: IllegalStateException) {
                context.reportError(ctx, e.message ?: "Failed to instantiate module!")
                return
            }
        } else {
            val dimensions = ctx.arraySize().map { arraySizes[it] ?: return }
            val fixedDimensions = dimensions.map { (it as? ArraySize.Fixed)?.size }.noNulls()

            if (fixedDimensions != null) {
                localSignals.forEach { (connection, signal) ->
                    val port = moduleType.ports[connection.port] ?: error("Missing expected port!")
                    var width = signal.width
                    fixedDimensions.forEachIndexed { index, dim ->
                        val curWidth = width
                        // special case for single bit wide ports
                        // this allows them to be assigned a value instead of an array aka sig[8] instead of sig[8][1]
                        if (curWidth is DefinedSimpleWidth && port.width is BitWidth && curWidth.size == dim) {
                            if (index != fixedDimensions.size - 1) {
                                context.reportError(
                                    connection.portCtx,
                                    "The signal \"${signal.name}\" does not match the dimensions of this module instance."
                                )
                            }
                            width = BitWidth
                        } else {
                            if (curWidth !is DefinedArrayWidth || curWidth.size != dim) {
                                context.reportError(
                                    connection.portCtx,
                                    "The signal \"${signal.name}\" does not match the dimensions of this module instance."
                                )
                                return
                            }
                            width = curWidth.next
                        }
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
                    fixedDimensions.forEach {
                        val curWidth = width
                        if (curWidth !is DefinedArrayWidth || curWidth.size != it) {
                            context.reportError(
                                param.portCtx,
                                "The parameter ${param.port} does not match the dimensions of this module instance."
                            )
                            return
                        }
                        width = curWidth.next
                    }
                    checkParameterWidth(
                        param.portCtx,
                        param.port,
                        moduleType.parameters[param.port]?.default?.width,
                        width
                    )
                }
            }

            // check for inouts in external connections
            // this isn't allowed since it would require connecting the inout to multiple module instances
            extSignals.forEach { (connection, _) ->
                val port = moduleType.ports[connection.port] ?: error("Missing expected port!")
                if (port.isInout) {
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
                context.notationCollector.createChild("ModuleInstanceArray($moduleInstanceName)"),
                moduleType,
                dimensions,
                mode = ExprEvalMode.Default,
                paramAssignments = paramCtx,
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
                    if (!this@TypesParser.context.mode.building) {
                        checkAllParameters()
                        if (notationCollector.hasErrors)
                            return
                        initialWalkAll()
                        if (notationCollector.hasErrors)
                            return
                        if (!checkPortDimensions()) {
                            context.reportError(
                                ctx,
                                "All module instances in an array must have identical port widths!"
                            )
                            return
                        }
                    }
                }
        }

        moduleInstances[instance.name] = instance
    }

    override suspend fun exitConList(ctx: ConListContext) {
        val signals = mutableListOf<Connection>()
        val params = mutableListOf<Connection>()
        ctx.connection().forEach { connectionContext ->
            connectionContext.sigCon()?.let { sigCtx ->
                val sigNameCtx = sigCtx.name() ?: return@let
                val name = sigNameCtx.text
                if (assignmentBlocks.any { block -> block.signals.any { it.port == name } })
                    context.reportError(
                        sigNameCtx,
                        "The port \"$name\" has already been assigned!"
                    )
                else {
                    val dynamicExpr = try {
                        DynamicExpr(sigCtx.expr() ?: return@let, context)
                    } catch (e: IllegalStateException) {
                        return@let
                    }
                    signals.add(Connection(sigNameCtx, dynamicExpr))
                }
            }
            connectionContext.paramCon()?.let { paramCtx ->
                val paramNameCtx = paramCtx.name() ?: return@let
                val name = paramNameCtx.text
                if (assignmentBlocks.any { block -> block.params.any { it.port == name } })
                    context.reportError(
                        paramNameCtx,
                        "The parameter \"$name\" has already been assigned!"
                    )
                else {
                    val dynamicExpr = try {
                        DynamicExpr(paramCtx.expr() ?: return@let, context)
                    } catch (e: IllegalStateException) {
                        return@let
                    }
                    params.add(Connection(paramNameCtx, dynamicExpr))
                }
            }
        }
        assignmentBlocks.add(AssignmentBlock(signals, params))
    }

    override suspend fun exitAssignBlock(ctx: AssignBlockContext) {
        assignmentBlocks.removeLast()
    }

    override suspend fun exitArraySize(ctx: ArraySizeContext) {
        val expr = context.expr.resolve(ctx.expr() ?: return)
        if (expr == null) {
            if (!context.mode.building) {
                context.reportError(ctx, "Array size expression couldn't be resolved.")
            }
            return
        }

        if (!expr.type.fixed)
            context.reportError(ctx, "Array sizes must be a constant value.")

        if ((context.mode.testing || context.mode.building) && expr.type == ExprType.Fixed && expr.value is UndefinedValue) { // uses parameters
            arraySizes[ctx] = ArraySize.Resolvable(ctx.expr() ?: return)
            return
        }

        if (expr.value is UndefinedValue) {
            return
        }

        if (expr.value !is SimpleValue || !expr.value.isNumber()) {
            context.reportError(ctx, "Array sizes must be a number.")
            return
        }

        val size = try {
            expr.value.toBigInt()!!.intValueExact() // isNumber() check above make !! safe
        } catch (e: ArithmeticException) {
            context.reportError(ctx, "Array size must fit into an integer.")
            return
        }

        if (size < 1) {
            context.reportError(ctx, "Array size must be greater than 0.")
            return
        }

        arraySizes[ctx] = ArraySize.Fixed(size)
    }


    override suspend fun exitSigDec(ctx: SigDecContext) {
        val signed = ctx.SIGNED() != null
        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text

        val isLocal = ctx.getParent() is AlwaysSignalContext

        if (nameCtx.TYPE_ID() == null)
            context.reportError(nameCtx, "Sig names must start with a lowercase letter.")

        if (context.resolveSignal(ctx, name) != null) {
            context.reportError(nameCtx, "The name \"$name\" is already in use.")
            return
        }

        val width = context.resolve(ctx.signalWidth())

        if (width == null) {
            if (!context.mode.building) {
                context.reportError(ctx.signalWidth() ?: ctx, "Failed to resolve signal width!")
            }
            return
        }

        if (isLocal) {
            val exprValue = ctx.expr()?.let { context.resolve(it) }?.value

            if (exprValue != null) {

                if (!width.canAssign(exprValue.width)) {
                    context.reportError(
                        ctx.expr()!!,
                        "Width of this expression isn't compatible with signal \"$name\"."
                    )
                    return
                }

                if (width.willTruncate(exprValue.width)) {
                    context.reportWarning(
                        ctx.expr()!!,
                        "The width of this expression is wider than the signal \"$name\" and will be truncated."
                    )
                }
            }

            val initValue = exprValue?.resizeToMatch(width) ?: width.filledWith(Bit.Bx, signed)

            val localSig = LocalSignal(name, initValue, signed, currentScope)
            localSignals.getOrPut(currentScope) { mutableMapOf() }[name] = localSig
        } else if (!sigs.contains(name)) {
            val dynamicExpr = ctx.expr()?.let { expr ->
                val value = context.resolve(expr)
                if (value == null) {
                    context.reportError(expr, "Failed to resolve signal assignment!")
                    return
                }

                val dynamicExpr = DynamicExpr(expr, context)

                if (!width.canAssign(dynamicExpr.width)) {
                    context.reportError(
                        expr,
                        "Width of this expression isn't compatible with signal \"$name\"."
                    )
                    return
                }

                if (width.willTruncate(dynamicExpr.width)) {
                    context.reportWarning(
                        expr,
                        "The width of this expression is wider than the signal \"$name\" and will be truncated."
                    )
                }

                dynamicExprs[expr] = dynamicExpr

                dynamicExpr
            }

            val init = dynamicExpr?.value?.resizeToMatch(width) ?: width.filledWith(Bit.Bx, signed)
            val signal = Signal(name, SignalDirection.Both, null, init, ExprType.Dynamic, signed)
            dynamicExpr?.let {
                it.connectTo(signal)
                signalDynamicExprs[signal] = it
            }
            sigs[name] = signal
        }
    }

    override suspend fun exitDffDec(ctx: DffDecContext) {
        val signed = ctx.SIGNED() != null

        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text

        if (nameCtx.TYPE_ID() == null)
            context.reportError(nameCtx, "Dff names must start with a lowercase letter.")

        if (context.resolveSignal(ctx, name) != null) {
            context.reportError(nameCtx, "The name \"$name\" is already in use.")
            return
        }

        var clk: DynamicExpr? = null
        var rst: DynamicExpr? = null
        var asyncRst = false
        val width = ctx.signalWidth()?.let { context.resolve(it) }

        if (width == null) {
            if (!context.mode.building) {
                context.reportError(ctx.signalWidth() ?: ctx, "Failed to resolve signal width!")
            }
            return
        }

        var init: Value = width.filledWith(Bit.B0, signed)

        val connectedSignals = mutableSetOf<String>()
        val connectedParams = mutableSetOf<String>()

        val signalConnections = mutableListOf<Connection>()
        val paramConnections = mutableListOf<Connection>()

        ctx.instCons()?.connection()?.forEach { connection ->
            connection.sigCon()?.let { sigCtx ->
                signalConnections.add(
                    Connection(
                        sigCtx.name() ?: return@let,
                        try {
                            DynamicExpr(sigCtx.expr() ?: return@let, context)
                        } catch (e: IllegalStateException) {
                            context.reportError(
                                sigCtx.expr() ?: ctx,
                                e.message ?: "Failed to create connection for \"${sigCtx.text}\""
                            )
                            return@let
                        }
                    )
                )
            }
            connection.paramCon()?.let { paramCtx ->
                paramConnections.add(
                    Connection(
                        paramCtx.name() ?: return@let,
                        try {
                            DynamicExpr(paramCtx.expr() ?: return@let, context)
                        } catch (e: IllegalStateException) {
                            context.reportError(
                                paramCtx.expr() ?: ctx,
                                e.message ?: "Failed to create connection for \"${paramCtx.text}\""
                            )
                            return@let
                        }
                    )
                )
            }
        }

        assignmentBlocks.forEach { block ->
            signalConnections.addAll(block.signals)
            paramConnections.addAll(block.params)
        }

        var initContext: ExprContext? = null
        paramConnections.forEach { param ->
            val paramName = param.port
            if (connectedParams.add(paramName)) {
                when (paramName) {
                    "INIT" -> param.value.value.let {
                        initContext = param.value.expr
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
                    "clk" -> clk = sig.value
                    "rst", "arst" -> {
                        if (rst != null) {
                            context.reportError(sig.portCtx, "DFFs can't have both \"rst\" and \"arst\" connections!")
                        }
                        rst = sig.value
                        asyncRst = sigName == "arst"
                    }

                    else -> context.reportError(
                        sig.portCtx,
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

        if (clkWidth.bitCount != 1) {
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
            if (rstWidth.bitCount != 1) {
                context.reportWarning(
                    resolvedClk.expr,
                    "The rst connection is wider than 1 bit and will be truncated."
                )
            }
        }

        val dff = Dff(
            context = context.project,
            name = name,
            init = init,
            clk = resolvedClk.constrain(BitWidth),
            rst = rst?.constrain(BitWidth),
            asyncReset = asyncRst,
            signed = signed,
            initContext = initContext
        )
        dffs[name] = dff
    }
}