package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.GlobalNamespace
import com.alchitry.labs.parsers.lucidv2.types.StructType

class GlobalParser(
    private val context: LucidGlobalContext
) : LucidBaseListener() {
    private val constants = mutableMapOf<String, Signal>()
    private val structs = mutableMapOf<String, StructType>()
    private val globals = mutableMapOf<String, GlobalNamespace>()

    fun resolveSignal(name: String) = constants[name]
    fun resolveGlobal(name: String) = globals[name]

    override fun enterGlobal(ctx: GlobalContext?) {
        constants.clear()
        structs.clear()
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
        if (constants.putIfAbsent(constName, Signal(constName, SignalDirection.Read, null, value)) != null) {
            context.reportError(
                ctx.name(),
                "The constant name $constName has already been used."
            )
        }
    }

    override fun exitStructDec(ctx: StructDecContext) {
        val type = context.resolve(ctx)
        if (type == null) {
            context.reportError(ctx, "Failed to resolve struct type!")
            return
        }
        if (structs.putIfAbsent(type.name, type) != null) {
            context.reportError(
                ctx.name(),
                "The struct name ${type.name} has already been used."
            )
        }
    }

    override fun exitGlobal(ctx: GlobalContext) {
        val name = ctx.name().text
        if (ctx.name().SPACE_ID() == null)
            context.reportError(
                ctx.name(),
                "Global names must start with an uppercase letter and contain at least one lowercase letter."
            )

        val global = GlobalNamespace(name, constants.mapValues { it.value.read(null) }, structs)
        if (!context.project.addGlobal(global)) {
            context.reportError(ctx.name(), "The global name $name has already been used.")
            return
        }
        globals[name] = global
    }
}