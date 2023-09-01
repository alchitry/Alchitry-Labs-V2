package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.grammar.LucidParser.ConstDecContext
import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
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
        val nameCtx = ctx.name() ?: return
        val constName = nameCtx.text
        if (nameCtx.CONST_ID() == null)
            context.reportError(nameCtx, "Constant names must be all uppercase letters.")

        val exprCtx = ctx.expr() ?: return
        val value = context.resolve(exprCtx)
        if (value == null) {
            context.reportError(exprCtx, "Failed to resolve constant value!")
            return
        }
        if (!value.constant) {
            context.reportError(
                exprCtx,
                "The value assigned to a constant must be constant!"
            )
            return
        }
        if (localConstants.putIfAbsent(constName, Signal(constName, SignalDirection.Read, null, value)) != null) {
            context.reportError(
                nameCtx,
                "The constant name \"$constName\" has already been used."
            )
            return
        }
        constants[ctx] = Constant(constName, value)
    }
}