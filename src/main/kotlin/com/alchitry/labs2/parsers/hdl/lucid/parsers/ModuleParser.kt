package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.asSingleLine
import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprContext
import com.alchitry.labs2.parsers.hdl.lucid.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.UndefinedValue
import com.alchitry.labs2.parsers.hdl.values.DefinedSimpleWidth
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class ModuleParser(
    private val context: LucidExprContext
) : LucidBaseListener(), SignalResolver {
    var modules = mutableListOf<Module>()
    private val localParams: MutableMap<String, Signal> = mutableMapOf()
    private val publicParams: MutableMap<String, Signal> = mutableMapOf()
    private var inConstraint: Boolean = false

    override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? =
        if (inConstraint) localParams[name] else publicParams[name]

    /**
     * Build a local reference to the default value
     */
    override fun exitParamDefault(ctx: LucidParser.ParamDefaultContext) {
        val parent = ctx.parent as? LucidParser.ParamDecContext ?: return
        val name = parent.name()?.text ?: return
        val defaultValue = ctx.expr()?.let { context.resolve(it)?.value } ?: UndefinedValue()

        if (defaultValue.width !is DefinedSimpleWidth) {
            context.reportError(ctx.expr() ?: ctx, "Default value must be a simple value.")
            return
        }

        val resizedValue = defaultValue.resizeToMatch(DefinedSimpleWidth(32)).withSign(true)
        if ((defaultValue as? SimpleValue)?.fitsIn(32, true) != true) {
            context.reportError(ctx.expr() ?: ctx, "Parameter values must fit into a 32bit signed value.")
            return
        }

        localParams[name] = Signal(
            name,
            SignalDirection.Read,
            null,
            resizedValue,
            ExprType.Fixed,
            signed = true
        )
    }

    override fun enterParamDec(ctx: LucidParser.ParamDecContext) {
        val name = ctx.name()?.text ?: return
        if (localParams.contains(name))
            return
        Signal(
            name,
            SignalDirection.Read,
            null,
            UndefinedValue(DefinedSimpleWidth(32)),
            ExprType.Fixed,
            signed = true
        ).also {
            localParams[name] = it
            publicParams[name] = it
        }
    }

    override fun exitParamDec(ctx: LucidParser.ParamDecContext) {
        if (ctx.paramDefault() == null) {
            val name = ctx.name() ?: return
            context.reportWarning(
                name,
                "No default value provided for parameter \"${name.text}\"." +
                        " Consider providing a default value using \"${name.text} = VALUE\"" +
                        " or a value to use only while error checking this file with \"${name.text} ~ VALUE\"."
            )
        }
    }

    override fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext) {
        inConstraint = true
    }

    override fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext) {
        inConstraint = false
        val value = context.resolve(ctx.expr() ?: return)?.value
        if (value is UndefinedValue)
            return
        if (value?.isTrue()?.bit != Bit.B1) {
            val defaultValue = (ctx.parent as? LucidParser.ParamDecContext)?.paramDefault()?.expr()
                ?.let { context.resolve(it) }
                ?.let {
                    if (DefinedSimpleWidth(32).canAssign(it.value.width)) {
                        it.copy(value = it.value.resizeToMatch(DefinedSimpleWidth(32)).withSign(true))
                    } else {
                        null
                    }
                }
            context.reportError(
                ctx,
                "Parameter constraint \"${ctx.text.asSingleLine()}\" failed for default value: $defaultValue"
            )
        }
    }

    override fun enterModule(ctx: LucidParser.ModuleContext) {
        localParams.clear()
        publicParams.clear()
        inConstraint = false
    }

    override fun exitModule(ctx: LucidParser.ModuleContext) {
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

            val defaultTestOnly = paramCtx.paramDefault()?.getChild(0)?.text == "~"
            val defaultValue = paramCtx.paramDefault()?.expr()?.let { context.resolve(it)?.value }

            val resizedValue = if (defaultValue != null && defaultValue.width is DefinedSimpleWidth) {
                defaultValue.resizeToMatch(DefinedSimpleWidth(32)).withSign(true)
            } else null

            val constraintContext = paramCtx.paramConstraint()?.expr()

            if (params.putIfAbsent(
                    paramName,
                    Parameter(paramName, resizedValue, defaultTestOnly, constraintContext)
                ) != null
            ) {
                context.reportError(
                    paramNameCtx,
                    "The parameter name \"$paramName\" has already been used."
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

            if (ports.putIfAbsent(portName, Port(portName, direction, width, signed, portCtx)) != null) {
                context.reportError(
                    portNameCtx,
                    "The port name \"$portName\" has already been used."
                )
            }
        }

        modules.add(
            Module(name, params, ports, ctx, context.sourceFile).also {
                if (!context.project.addModule(it)) {
                    context.reportError(ctx.name() ?: ctx, "A module with name $name already exists!")
                }
            }
        )

    }
}