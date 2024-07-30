package com.alchitry.labs2.parsers.hdl.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.LucidParser.ConstDecContext
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucidv2.context.LucidExprContext
import com.alchitry.labs2.parsers.hdl.lucidv2.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.types.Constant
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import com.alchitry.labs2.parsers.hdl.values.UndefinedValue
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class ConstantParser(
    private val context: LucidExprContext,
    private val constants: MutableMap<ConstDecContext, Constant> = mutableMapOf(),
    val localConstants: MutableMap<String, Signal> = mutableMapOf()
) : LucidBaseListener(), SignalResolver {
    override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? = localConstants[name]
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
        val expr = context.resolve(exprCtx)
        val value = expr?.value ?: UndefinedValue()
        if (expr?.type != ExprType.Constant) {
            context.reportError(
                exprCtx,
                "The value assigned to a constant must be constant!"
            )
            return
        }
        if (localConstants.putIfAbsent(
                constName,
                Signal(constName, SignalDirection.Read, null, value, ExprType.Constant)
            ) != null
        ) {
            context.reportError(
                nameCtx,
                "The constant name \"$constName\" has already been used."
            )
            return
        }
        constants[ctx] = Constant(constName, value)
    }
}