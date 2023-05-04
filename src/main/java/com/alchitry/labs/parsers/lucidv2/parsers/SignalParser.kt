package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.context.StructResolver
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.*
import org.antlr.v4.runtime.ParserRuleContext

class SignalParser(
    private val context: LucidModuleContext
) : LucidBaseListener(), SignalResolver, StructResolver {
    private val dffs = mutableMapOf<String, Dff>()
    private val sigs = mutableMapOf<String, Signal>()

    private val localStructType = mutableMapOf<String, StructType>()
    private val structTypes = mutableMapOf<StructDecContext, StructType>()
    private val resolvedStructTypes = mutableMapOf<StructTypeContext, StructType>()
    private val signalWidths = mutableMapOf<SignalWidthContext, SignalWidth>()
    private val signals = mutableMapOf<SignalContext, SignalOrSubSignal>()
    private val arraySizes = mutableMapOf<ArraySizeContext, Int>()
    private val assignmentBlocks = mutableListOf<AssignmentBlock>()

    private val localParams = mutableMapOf<String, Signal>()

    private var inModule = false
    private var inParamDec = false

    /** Assumes the name is a simple name (no .'s) */
    override fun resolve(name: String): SignalOrParent? {
        dffs[name]?.let { return it }
        sigs[name]?.let { return it }

        // check the instance for a parameter value before resorting to the local version
        context.instance?.parameters?.get(name)?.let { return it }
        localParams[name]?.let { return it }

        return null
    }

    fun resolve(sigCtx: SignalContext): SignalOrSubSignal? = signals[sigCtx]

    fun resolveStructType(ctx: StructDecContext) = structTypes[ctx]
    override fun resolve(ctx: StructTypeContext): StructType? = resolvedStructTypes[ctx]
    fun resolveSignalWidth(ctx: SignalWidthContext): SignalWidth? = signalWidths[ctx]

    override fun enterParamDec(ctx: ParamDecContext) {
        inParamDec = true
    }

    override fun exitParamDec(ctx: ParamDecContext) {
        inParamDec = false
    }

    /**
     * Build a local reference to the default value
     */
    override fun enterParamConstraint(ctx: ParamConstraintContext) {
        val parent = ctx.parent
        if (parent is ParamDecContext) {
            val name = parent.name().text
            val defaultValue = parent.paramDefault()?.expr()?.let { context.expr.resolve(it) } ?: UndefinedValue(true)
            localParams[name] = Signal(name, SignalDirection.Read, null, defaultValue)
        }
    }

    override fun exitParamConstraint(ctx: ParamConstraintContext) {
        val value = context.expr.resolve(ctx.expr())
        if (value?.isTrue()?.bit != Bit.B1) {
            context.errorCollector.reportError(ctx, "Parameter constraint \"${ctx.text}\" failed!")
        }
    }

    override fun enterModule(ctx: ModuleContext) {
        inModule = true
        localStructType.clear()
    }

    override fun exitModule(ctx: ModuleContext) {
        inModule = false
    }

    override fun exitSignal(ctx: SignalContext) {
        val firstName = ctx.name().firstOrNull() ?: return
        val signalOrParent = context.resolveSignal(firstName.text)

        if (signalOrParent == null) {
            context.errorCollector.reportError(ctx.name(0), "Failed to resolve signal $firstName")
            return
        }

        val children = ctx.children.filter { it is NameContext || it is BitSelectionContext }
        val usedChildren: Int

        val signal = when (signalOrParent) {
            is Signal -> {
                usedChildren = 1
                signalOrParent
            }

            is SignalParent -> {
                usedChildren = 2
                if (children.size < 2) {
                    context.errorCollector.reportError(
                        ctx.name(0),
                        "$firstName is not a signal and can't be accessed directly."
                    )
                    return
                }
                if (children[1] is BitSelectionContext) {
                    context.errorCollector.reportError(children[1] as ParserRuleContext, "$firstName is not an array.")
                    return
                }
                val sig = signalOrParent.getSignal(children[1].text)
                if (sig == null) {
                    context.errorCollector.reportError(
                        children[1] as ParserRuleContext,
                        "Failed to resolve signal $firstName.${children[1].text}"
                    )
                    return
                }
                sig
            }
        }

        val selectionMap = children.subList(usedChildren, children.size).flatMap { child ->
            when (child) {
                is NameContext -> listOf(SignalSelector.Struct(child.text) to child)
                is BitSelectionContext -> context.expr.resolve(child).map { SignalSelector.Bits(it.range) to it.ctx }
                else -> error("Signal child was not a NameContext or BitSelectionContext")
            }
        }.toMap()

        val sigSelection = selectionMap.keys.toList()

        val selectedSignal = if (sigSelection.isEmpty()) {
            signal
        } else {
            try {
                signal.select(sigSelection)
            } catch (e: SignalSelectionException) {
                context.errorCollector.reportError(selectionMap[e.selector]!!, e.message!!)
                return
            }
        }

        signals[ctx] = selectedSignal
    }

    override fun exitSignalWidth(ctx: SignalWidthContext) {
        val dims = ctx.arraySize()
            .asReversed()
            .mapNotNull { (context.expr.resolve(it.expr()) as? BitListValue)?.toBigInt()?.intValueExact() }

        val structType = ctx.structType()?.let {
            resolvedStructTypes[it].also { v ->
                if (v == null)
                    context.errorCollector.reportError(
                        ctx.structType(),
                        "Failed to resolve struct type ${it.text}!"
                    )
            }
        }

        if (dims.isEmpty()) {
            signalWidths[ctx] = structType?.let { StructWidth(it) } ?: BitWidth
            return
        }

        val base = structType?.let { StructWidth(it) } ?: BitListWidth(dims[0])

        if (dims.size == 1 && structType == null) {
            signalWidths[ctx] = base
            return
        }

        var lastWidth: SignalWidth = base

        val offset = if (structType == null) 1 else 0

        repeat(dims.size - offset) {
            lastWidth = ArrayWidth(dims[it + offset], lastWidth)
        }

        signalWidths[ctx] = lastWidth
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

    override fun exitStructType(ctx: StructTypeContext) {
        val name = ctx.name().firstOrNull()?.text ?: return

        val type = if (ctx.name().size > 1) { // includes a . aka GlobalSpace.structName
            val global = context.project.resolveGlobal(name)
            if (global == null) {
                context.errorCollector.reportError(ctx.name(0), "Couldn't find global namespace $name")
                return
            }

            if (ctx.name().size > 2) {
                context.errorCollector.reportError(ctx.name(2), "Unknown extension to struct name.")
                return
            }

            global.structs[ctx.name(1).text]
        } else { // local struct
            localStructType[name]
        }

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

            val width = signalWidths[structMemberContext.signalWidth()]

            if (width == null) {
                context.errorCollector.reportError(structMemberContext.signalWidth(), "Failed to resolve signal width!")
                return@forEach
            }

            members[memberName] = StructMember(memberName, width, signed)
        }

        structTypes[ctx] = StructType(name, members).also {
            if (inModule)
                localStructType[name] = it
        }
    }

    override fun exitSigDec(ctx: SigDecContext) {
        val signed = ctx.SIGNED() != null
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.errorCollector.reportError(ctx.name(), "Sig names must start with a lowercase letter.")

        val width = signalWidths[ctx.signalWidth()]

        if (width == null) {
            context.errorCollector.reportError(ctx.signalWidth(), "Failed to resolve signal width!")
            return
        }

        sigs[name] = Signal(name, SignalDirection.Both, null, width.filledWith(Bit.Bu, false, signed), signed)
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val signed = ctx.SIGNED() != null

        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.errorCollector.reportError(ctx.name(), "Dff names must start with a lowercase letter.")

        var clk: DynamicExpr? = null
        var rst: DynamicExpr? = null
        val width = signalWidths[ctx.signalWidth()]

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

        val dff = Dff(name, init, resolvedClk.constrain(BitWidth), rst?.constrain(BitWidth), signed)
        dffs[name] = dff
    }
}