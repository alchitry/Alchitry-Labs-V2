package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.GlobalNamespace
import com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.values.Value

data class GlobalParser(
    private val context: LucidModuleContext
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)

    override fun exitGlobal(ctx: LucidParser.GlobalContext) {
        val name = ctx.name().text
        if (ctx.name().SPACE_ID() == null)
            context.errorCollector.reportError(
                ctx.name(),
                "Global names must start with an uppercase letter and contain at least one lowercase letter."
            )

        val structs = mutableMapOf<String, StructType>()
        val constants = mutableMapOf<String, Value>()

        ctx.globalStat().forEach { statCtx ->
            statCtx.constDec()?.let { constCtx ->
                val constName = constCtx.name().text
                if (constCtx.name().CONST_ID() == null)
                    context.errorCollector.reportError(constCtx.name(), "Constant names must be all uppercase letters.")

                val value = context.expr.resolve(constCtx.expr())
                if (value == null) {
                    context.errorCollector.reportError(constCtx.expr(), "Failed to resolve constant value!")
                    return@let
                }
                if (!value.constant) {
                    context.errorCollector.reportError(
                        constCtx.expr(),
                        "The value assigned to a constant must be constant!"
                    )
                    return@let
                }
                if (constants.putIfAbsent(constName, value) != null) {
                    context.errorCollector.reportError(
                        constCtx.name(),
                        "The constant name $constName has already been used."
                    )
                }
            }

            statCtx.structDec()?.let { structCtx ->
                val type = context.signal.resolveStructType(structCtx)
                if (type == null) {
                    context.errorCollector.reportError(structCtx, "Failed to resolve struct type!")
                    return@let
                }
                if (structs.putIfAbsent(type.name, type) != null) {
                    context.errorCollector.reportError(
                        structCtx.name(),
                        "The struct name ${type.name} has already been used."
                    )
                }
            }
        }

        if (!context.project.addGlobal(GlobalNamespace(name, constants, structs))) {
            context.errorCollector.reportError(ctx.name(), "The global name $name has already been used.")
        }
    }
}