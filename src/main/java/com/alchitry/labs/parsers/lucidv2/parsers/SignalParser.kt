package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.*
import org.antlr.v4.runtime.ParserRuleContext

fun ParserRuleContext.firstParentOrNull(condition: (ParserRuleContext) -> Boolean): ParserRuleContext? {
    var context = this.getParent() ?: return null
    while (!condition(context)) {
        context = context.getParent() ?: return null
    }
    return context
}

data class SignalParser(
    private val context: LucidExprContext,
    private val signals: MutableMap<SignalContext, SignalOrSubSignal> = mutableMapOf(),
    private val signalWidths: MutableMap<SignalWidthContext, SignalWidth> = mutableMapOf(),
    private val localRepeatSignals: MutableMap<String, Signal> = mutableMapOf()
) : LucidBaseListener() {
    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolve(sigCtx: SignalContext): SignalOrSubSignal? = signals[sigCtx]
    fun resolve(ctx: SignalWidthContext): SignalWidth? = signalWidths[ctx]

    override fun enterRepeatBlock(ctx: RepeatBlockContext) {
        val repCtx = ctx.getParent() as RepeatStatContext
        if (context is LucidModuleContext) {
            val alwaysParent = (ctx.firstParentOrNull { it is AlwaysBlockContext }
                ?: error("Repeat statement without an AlwaysBlock parent?")) as AlwaysBlockContext

            val repeatSignals =
                context.alwaysParser.alwaysBlocks[alwaysParent]?.repeatSignals ?: context.alwaysParser.repeatSignals
            val newSig = repeatSignals[repCtx] ?: error("Missing repeat signal for repeat block!")
            localRepeatSignals[newSig.name] = newSig
        }
    }

    override fun exitRepeatBlock(ctx: RepeatBlockContext) {
        val repCtx = ctx.getParent() as RepeatStatContext
        localRepeatSignals.remove(repCtx.name().text)
    }

    override fun exitSignal(ctx: SignalContext) {
        val firstName = ctx.name().firstOrNull() ?: return
        val signalOrParent = localRepeatSignals[firstName.text] ?: context.resolveSignal(firstName.text)

        if (signalOrParent == null) {
            context.reportError(ctx.name(0), "Failed to resolve signal ${firstName.text}")
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
                    context.reportError(
                        ctx.name(0),
                        "$firstName is not a signal and can't be accessed directly."
                    )
                    return
                }
                if (children[1] is BitSelectionContext) {
                    context.reportError(children[1] as ParserRuleContext, "${firstName.text} is not an array.")
                    return
                }
                val sig = signalOrParent.getSignal(children[1].text)
                if (sig == null) {
                    context.reportError(
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
                is BitSelectionContext -> context.resolve(child)
                    .map { SignalSelector.Bits(it.range) to it.ctx }

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
                context.reportError(selectionMap[e.selector]!!, e.message!!)
                return
            }
        }

        signals[ctx] = selectedSignal
    }

    override fun exitSignalWidth(ctx: SignalWidthContext) {
        val dims = ctx.arraySize()
            .asReversed()
            .mapNotNull { (context.resolve(it.expr()) as? BitListValue)?.toBigInt()?.intValueExact() }

        val structType = ctx.structType()?.let {
            context.resolve(it).also { v ->
                if (v == null)
                    context.reportError(
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
}