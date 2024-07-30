package com.alchitry.labs2.parsers.hdl.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.lucidv2.context.LucidExprContext
import com.alchitry.labs2.parsers.hdl.lucidv2.values.*
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.Function
import org.antlr.v4.kotlinruntime.ParserRuleContext

fun ParserRuleContext.firstParentOrNull(condition: (ParserRuleContext) -> Boolean): ParserRuleContext? {
    var context = this.parent as? ParserRuleContext ?: return null
    while (!condition(context)) {
        context = context.parent as? ParserRuleContext ?: return null
    }
    return context
}

data class SignalParser(
    private val context: LucidExprContext,
    private val signals: MutableMap<SignalContext, SignalOrSubSignal> = mutableMapOf(),
    private val signalWidths: MutableMap<SignalWidthContext, SignalWidth> = mutableMapOf()
) : LucidBaseListener() {
    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolve(sigCtx: SignalContext): SignalOrSubSignal? = signals[sigCtx]
    fun resolve(ctx: SignalWidthContext): SignalWidth? = signalWidths[ctx]

    override fun enterRepeatBlock(ctx: RepeatBlockContext) {
        if (context !is LucidBlockContext || context.stage == ParseStage.Evaluation)
            return

        val repCtx = ctx.parent as? RepeatStatContext ?: error("RepeatBlockContext parent wasn't a RepeatStatContext!")

        val repeatSignals = context.blockParser.repeatSignals
        val newSig = repeatSignals[repCtx] ?: return
        context.localSignals[newSig.name] = newSig
    }

    override fun exitRepeatBlock(ctx: RepeatBlockContext) {
        if (context !is LucidBlockContext || context.stage == ParseStage.Evaluation)
            return

        val repCtx = ctx.parent as? RepeatStatContext ?: error("RepeatBlockContext parent wasn't a RepeatStatContext!")
        val repeatBlock = context.blockParser.resolveRepeatBlock(repCtx) ?: return
        context.localSignals.remove(repeatBlock.signal)
    }

    override fun enterFunctionBody(ctx: FunctionBodyContext) {
        if (context !is LucidBlockContext || context.stage == ParseStage.Evaluation)
            return

        val funcCtx =
            ctx.parent as? FunctionBlockContext ?: error("FunctionBodyContext parent wasn't a FunctionBlockContext!")

        val function = context.resolveFunction(funcCtx.name()?.text ?: return) as? Function.Custom ?: return
        function.args.forEach {
            context.localSignals[it.name] =
                Signal(it.name, SignalDirection.Read, null, it.width.filledWith(Bit.B0, it.signed), ExprType.Known)
        }
    }

    override fun exitFunctionBody(ctx: FunctionBodyContext) {
        if (context !is LucidBlockContext || context.stage == ParseStage.Evaluation)
            return

        val funcCtx =
            ctx.parent as? FunctionBlockContext ?: error("FunctionBodyContext parent wasn't a FunctionBlockContext!")

        val function = context.resolveFunction(funcCtx.name()?.text ?: return) as? Function.Custom ?: return
        function.args.forEach {
            context.localSignals.remove(it.name)
        }
    }

    override fun exitSignal(ctx: SignalContext) {
        val children = ctx.children?.filter { it is NameContext || it is BitSelectionContext } ?: emptyList()
        var currentSignalOrParent: SignalOrParent

        val nameCtx = ctx.name()
        val firstName = nameCtx.firstOrNull() ?: return
        val signalOrParent = context.resolveSignal(ctx, firstName.text)

        if (signalOrParent == null) {
            // check if the signal is the first argument in a repeat block
            if (ctx.name().size == 1 && ctx.bitSelection().isEmpty()) {
                val parent = ctx.parent
                if (parent is ExprSignalContext) {
                    val pp = parent.parent
                    if (pp is RepeatStatContext && pp.expr(0) == parent) {
                        return
                    }
                }
            }

            context.reportError(nameCtx.first(), "Failed to resolve signal \"${firstName.text}\"")
            return
        }

        var usedChildren = 1
        currentSignalOrParent = signalOrParent

        while (currentSignalOrParent is SignalParent) {
            if (children.size < usedChildren + 1) {
                if (currentSignalOrParent is EnumType) {
                    val functionName =
                        (ctx.parent?.parent?.parent as? FunctionContext)?.FUNCTION_ID()?.text?.substring(1)
                    if (functionName != null && context.resolveFunction(functionName) == Function.WIDTH)
                        return
                }
                context.reportError(
                    nameCtx[usedChildren - 1],
                    "${nameCtx[usedChildren - 1].text} is not a signal and can't be accessed directly."
                )
                return
            }
            if (children[usedChildren] is BitSelectionContext) {
                context.reportError(
                    children[usedChildren] as ParserRuleContext,
                    "${nameCtx[usedChildren].text} is not an array."
                )
                return
            }
            val sig = currentSignalOrParent.getSignal(children[usedChildren].text)
            if (sig == null) {
                context.reportError(
                    children[usedChildren] as ParserRuleContext,
                    "Failed to resolve signal ${firstName.text}.${children[usedChildren].text}"
                )
                return
            }
            usedChildren += 1
            currentSignalOrParent = sig
        }

        val signal = currentSignalOrParent as Signal

        val selectionChildren = children.subList(usedChildren, children.size)
        val selectionMap = selectionChildren.flatMap { child ->
            when (child) {
                is NameContext -> listOf(SignalSelector.Struct(child.text) to child)
                is BitSelectionContext -> context.resolve(child)
                    .map { SignalSelector.Bits(it.range, it.selectionCtx, it.undefined) to it.ctx }

                else -> error("Signal child was not a NameContext or BitSelectionContext")
            }
        }.toMap()

        // don't skip local signals as they change
        if (context !is LucidBlockContext || !context.localSignals.contains(signal.name))
            ctx.skip = selectionChildren.all {
                when (it) {
                    is NameContext -> true
                    is BitSelectionContext -> it.skip
                    else -> error("Signal child was not a NameContext or BitSelectionContext")
                }
            }

        val sigSelection = selectionMap.keys.toList()

        val selectedSignal = if (sigSelection.isEmpty()) {
            signal
        } else {
            try {
                signal.select(sigSelection)
            } catch (e: SignalSelectionException) {
                if (!context.inDeadBlock(ctx)) {
                    context.reportError(selectionMap[e.selector]!!, e.message!!)
                }
                return
            }
        }

        signals[ctx] = selectedSignal
    }

    override fun exitSignalWidth(ctx: SignalWidthContext) {
        val structType = ctx.structType()?.let {
            context.resolve(it).also { v ->
                if (v == null)
                    context.reportError(
                        ctx.structType() ?: ctx,
                        "Failed to resolve struct type ${it.text}!"
                    )
            }
        }

        val dims = ctx.arraySize().asReversed().map { it.expr() ?: return }

        if (dims.isEmpty()) {
            signalWidths[ctx] = structType?.let { StructWidth(it) } ?: BitWidth
            return
        }

        var current: SignalWidth? = structType?.let { StructWidth(it) }
        dims.forEach { d ->
            val v = context.resolve(d)?.value
            current = when (v) {
                is SimpleValue -> {
                    val size = v.toBigInt()?.toInt() ?: return
                    if (size <= 0) {
                        context.reportError(d, "Array widths must be greater than 0.")
                    }
                    val adjSize = size.coerceAtLeast(1)
                    current?.let { DefinedArrayWidth(adjSize, it) } ?: BitListWidth(adjSize)
                }

                is UndefinedValue -> {
                    current?.let { ResolvableArrayWidth(d, it) } ?: ResolvableSimpleWidth(d)
                }

                else -> return
            }
        }

        signalWidths[ctx] = current ?: return
    }
}