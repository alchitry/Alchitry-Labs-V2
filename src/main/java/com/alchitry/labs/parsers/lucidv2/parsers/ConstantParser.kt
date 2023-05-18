package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ConstDecContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.Constant

data class ConstantParser(
    private val context: LucidExprContext,
    private val constants: MutableMap<ConstDecContext, Constant> = mutableMapOf(),
    private val localConstants: MutableMap<String, Signal> = mutableMapOf()
) : LucidBaseListener(), SignalResolver {
    override fun resolve(name: String) = localConstants[name]
    fun resolve(constDecContext: ConstDecContext) = constants[constDecContext]

    override fun enterGlobal(ctx: LucidParser.GlobalContext) {
        localConstants.clear()
    }

    override fun enterModule(ctx: LucidParser.ModuleContext) {
        localConstants.clear()
    }

    override fun exitConstDec(ctx: ConstDecContext) {
        val constName = ctx.name().text
        if (ctx.name().CONST_ID() == null)
            context.reportError(ctx.name(), "Constant names must be all uppercase letters.")

        val value = context.resolve(ctx.expr())
        if (value == null) {
            context.reportError(ctx.expr(), "Failed to resolve constant value!")
            return
        }
        if (!value.constant) {
            context.reportError(
                ctx.expr(),
                "The value assigned to a constant must be constant!"
            )
            return
        }
        if (localConstants.putIfAbsent(constName, Signal(constName, SignalDirection.Read, null, value)) != null) {
            context.reportError(
                ctx.name(),
                "The constant name \"$constName\" has already been used."
            )
            return
        }
        constants[ctx] = Constant(constName, value)
    }
}