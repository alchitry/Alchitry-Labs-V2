package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.Module
import com.alchitry.labs.parsers.lucidv2.signals.Parameter
import com.alchitry.labs.parsers.lucidv2.signals.Port
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection

data class ModuleParser(
    private val context: LucidModuleContext,
    var module: Module? = null
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)

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
            val signed = portCtx.SIGNED() != null

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

            ports[portName] = Port(portName, direction, width, signed)
        }

        module = Module(name, params, ports).also {
            if (!context.project.addModule(it)) {
                context.errorCollector.reportError(ctx.name(), "A module with name $name already exists!")
            }
        }

    }
}