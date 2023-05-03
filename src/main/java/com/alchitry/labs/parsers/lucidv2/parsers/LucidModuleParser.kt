package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.Value

class LucidModuleParser(private val context: LucidModuleContext) : LucidBaseListener() {
    var module: Module? = null
        private set

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

    override fun exitModule(ctx: LucidParser.ModuleContext) {
        if (module != null) {
            context.errorCollector.reportError(ctx, "Only one module per file is allowed.")
            return
        }

        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null)
            context.errorCollector.reportError(ctx.name(), "Module names must start with a lowercase letter!")

        val params = mutableMapOf<String, Parameter>()
        val ports = mutableMapOf<String, Port>()

        ctx.paramList()?.paramDec()?.forEach { paramCtx ->
            val paramName = paramCtx.name().text
            if (paramCtx.name().CONST_ID() == null) {
                context.errorCollector.reportError(
                    paramCtx.name(),
                    "Parameter names must start with an uppercase letter and can only contain uppercase letters, numbers, and underscores."
                )
            }

            val defaultValue = paramCtx.paramDefault()?.expr()?.let { context.expr.resolve(it) }
            val constraintContext = paramCtx.paramConstraint()?.expr()

            if (params.putIfAbsent(paramName, Parameter(paramName, defaultValue, constraintContext)) != null) {
                context.errorCollector.reportError(
                    paramCtx.name(),
                    "The parameter name $paramName has already been used."
                )
            }
        }

        ctx.portList().portDec().forEach { portCtx ->
            val portName = portCtx.name().text
            if (portCtx.name().TYPE_ID() == null) {
                context.errorCollector.reportError(portCtx.name(), "Port names must start with a lowercase letter!")
            }

            val direction = when (portCtx.portDirection().text) {
                "input" -> SignalDirection.Read
                "output" -> SignalDirection.Write
                "inout" -> SignalDirection.Both
                else -> {
                    context.errorCollector.reportError(
                        portCtx.portDirection(),
                        "Unknown signal type ${portCtx.portDirection().text}!"
                    )
                    return
                }
            }

            val width = context.signal.resolveSignalWidth(portCtx.signalWidth())
            if (width == null) {
                context.errorCollector.reportError(portCtx.signalWidth(), "Failed to resolve signal width!")
                return@forEach
            }

            ports[portName] = Port(portName, direction, width)
        }

        module = Module(name, params, ports).also {
            if (!context.project.addModule(it)) {
                context.errorCollector.reportError(ctx.name(), "A module with name $name already exists!")
            }
        }

    }
}