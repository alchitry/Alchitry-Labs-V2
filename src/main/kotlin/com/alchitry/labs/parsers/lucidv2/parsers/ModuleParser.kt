package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.Parameter
import com.alchitry.labs.parsers.lucidv2.types.Signal
import com.alchitry.labs.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.ports.Port
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.UndefinedValue

data class ModuleParser(
    private val context: LucidExprContext
) : LucidBaseListener(), SignalResolver {
    var module: Module? = null
    private val localParams: MutableMap<String, Signal> = mutableMapOf()
    private val publicParams: MutableMap<String, Signal> = mutableMapOf()
    private var inConstraint: Boolean = false

    override fun resolve(name: String) = if (inConstraint) localParams[name] else publicParams[name]

    /**
     * Build a local reference to the default value
     */
    override fun exitParamDefault(ctx: LucidParser.ParamDefaultContext) {
        val parent = ctx.parent
        if (parent is LucidParser.ParamDecContext) {
            val name = parent.name()?.text ?: return
            val defaultValue = parent.paramDefault()?.expr()?.let { context.resolve(it) } ?: UndefinedValue(true)
            localParams[name] = Signal(name, SignalDirection.Read, null, defaultValue)
        }
    }

    override fun enterParamDec(ctx: LucidParser.ParamDecContext) {
        val name = ctx.name()?.text ?: return
        if (localParams.contains(name))
            return
        Signal(name, SignalDirection.Read, null, UndefinedValue(true)).also {
            localParams[name] = it
            publicParams[name] = it
        }
    }

    override fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext) {
        inConstraint = true
    }

    override fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext) {
        inConstraint = false
        val value = context.resolve(ctx.expr() ?: return)
        if (value is UndefinedValue)
            return
        if (value?.isTrue()?.bit != Bit.B1) {
            val defaultValue = (ctx.parent as? LucidParser.ParamDecContext)?.paramDefault()?.expr()
                ?.let { context.resolve(it) }
            context.reportError(ctx, "Parameter constraint \"${ctx.text}\" failed for default value: $defaultValue")
        }
    }


    override fun exitModule(ctx: LucidParser.ModuleContext) {
        if (module != null) {
            context.reportError(ctx, "Only one module per file is allowed.")
            return
        }

        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text

        if (nameCtx.TYPE_ID() == null)
            context.reportError(nameCtx, "Module names must start with a lowercase letter!")

        val params = mutableMapOf<String, Parameter>()
        val ports = mutableMapOf<String, Port>()

        ctx.paramList()?.paramDec()?.forEach { paramCtx ->
            val paramNameCtx = paramCtx.name() ?: return@forEach
            val paramName = paramNameCtx.text
            if (paramNameCtx.CONST_ID() == null) {
                context.reportError(
                    paramNameCtx,
                    "Parameter names must start with an uppercase letter and can only contain uppercase letters, numbers, and underscores."
                )
            }

            val defaultValue = paramCtx.paramDefault()?.expr()?.let { context.resolve(it) }
            val constraintContext = paramCtx.paramConstraint()?.expr()

            if (params.putIfAbsent(paramName, Parameter(paramName, defaultValue, constraintContext)) != null) {
                context.reportError(
                    paramNameCtx,
                    "The parameter name $paramName has already been used."
                )
            }
        }

        ctx.portList()?.portDec()?.forEach { portCtx ->
            val signed = portCtx.SIGNED() != null

            val portNameCtx = portCtx.name() ?: return@forEach
            val portName = portNameCtx.text
            if (portNameCtx.TYPE_ID() == null) {
                context.reportError(portNameCtx, "Port names must start with a lowercase letter!")
            }

            val direction = when (portCtx.portDirection()?.text) {
                "input" -> SignalDirection.Read
                "output" -> SignalDirection.Write
                "inout" -> SignalDirection.Both
                else -> {
                    context.reportError(
                        portCtx.portDirection() ?: portCtx,
                        "Unknown signal type ${portCtx.portDirection()?.text}!"
                    )
                    return
                }
            }

            val width = portCtx.signalWidth()?.let { context.resolve(it) }
            if (width == null) {
                context.reportError(portCtx.signalWidth() ?: portCtx, "Failed to resolve signal width!")
                return@forEach
            }

            ports[portName] = Port(portName, direction, width, signed)
        }

        module = Module(name, params, ports, ctx).also {
            if (!context.project.addModule(it)) {
                context.reportError(ctx.name() ?: ctx, "A module with name $name already exists!")
            }
        }

    }
}