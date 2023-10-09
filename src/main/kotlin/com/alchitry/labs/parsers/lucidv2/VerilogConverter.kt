package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.Env
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.NotationType
import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.parsers.ExprParser
import com.alchitry.labs.parsers.lucidv2.types.*
import com.alchitry.labs.parsers.lucidv2.values.*
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree

class VerilogConverter(
    private val context: LucidBlockContext
) : LucidBaseListener() {
    val verilog = mutableMapOf<ParseTree, String>()
    private var tabCount: Int = 0
    private val globals = mutableSetOf<Signal>()

    /**
     * Throws an [IllegalStateException] with the provided message and line/offset of the provided context.
     */
    private fun error(ctx: ParserRuleContext, message: String): Nothing {
        val notation = ErrorListener.getNotation(ctx, message, NotationType.Error)
        error(notation.toString())
    }

    /**
     * Adds a new line and indents the current [tabCount]
     */
    private fun StringBuilder.newLine(): StringBuilder {
        append("\n")
        for (i in 0..<tabCount)
            append("  ")
        return this
    }

    /**
     * Checks if the expression is constant and can be replaced by a simple value.
     * @return true if the expression was handled, false otherwise
     */
    private fun handleConstant(ctx: LucidParser.ExprContext): Boolean {
        val value = context.resolve(ctx) ?: return false
        if (value.constant) {
            ctx.verilog = value.flatten().asVerilog() ?: error(ctx, "$value could not be converted to verilog!")
            return true
        }
        return false
    }

    /**
     * Convenient getter/setter for the internal verilog map.
     * It throws an [IllegalStateException] if the verilog is missing for the [ParseTree].
     */
    private var ParseTree.verilog: String
        get() = this@VerilogConverter.verilog[this]
            ?: error("Missing verilog for ${this::class.simpleName}: \"${this.text}\"")
        set(value) {
            this@VerilogConverter.verilog[this] = value
        }

    /**
     * Throws an [IllegalStateException] if the value is null with a message containing the location of [context].
     * @param context The context to use as the error location if this is null.
     */
    private inline fun <reified T : ParseTree> T?.requireNotNull(context: ParserRuleContext): T {
        return this ?: error(context, "Context \"${T::class.simpleName}\" was null!")
    }

    private fun StringBuilder.addParamHeader() {
        val parameters = (context.instance as ModuleInstance).parameters
        if (parameters.isEmpty())
            return
        append("/*\n  Parameters:\n")
        parameters.values.forEach { param ->
            append("      ")
            append(param.name)
            append(" = ")
            append(param.initialValue)
            append("\n")
        }
        append("*/\n")
    }

    private fun StringBuilder.addPorts(ports: Collection<Signal>) {
        append(" (")
        tabCount++
        ports.forEachIndexed { index, port ->
            newLine()
            when (port.direction) {
                SignalDirection.Read -> append("input ")
                SignalDirection.Write -> append("output reg ")
                SignalDirection.Both -> append("inout ")
            }
            if (port.signed)
                append("signed ")
            val bitCount = port.width.bitCount ?: error("Module port \"${port.name}\" has undefined width!")
            if (bitCount > 1) {
                append("[")
                append(bitCount - 1)
                append(":0] ")
            }
            append(port.verilogName)
            if (index != ports.size - 1)
                append(",")
        }
        tabCount--
        newLine()
        append(");")
    }

    private fun StringBuilder.addConstants() {
        val instance = context.instance as ModuleInstance
        val constants = mutableListOf<Signal>().apply {
            addAll(globals)
            addAll(instance.parameters.values)
            addAll(context.constant.localConstants.values)
            context.enum.localEnumType.values.forEach {
                add(it.widthSignal)
                addAll(it.memberSignals.values)
            }
        }

        constants.forEach {
            append("localparam ")
            append(it.verilogName)
            append(" = ")
            append(it.initialValue.flatten().asVerilog())
            append(";")
            newLine()
        }
    }

    private fun StringBuilder.addDffs() {
        context.types.dffs.values.forEach { dff ->
            append("reg ")
            if (dff.signed)
                append("signed ")
            val bitCount = dff.d.width.bitCount ?: error("Dff \"${dff.name}\" has an undefined width!")
            if (bitCount > 1) {
                append("[")
                append(bitCount - 1)
                append(":0] ")
            }
            append(dff.d.verilogName)
            append(", ")
            append(dff.q.verilogName)
            append(" = ")
            append(dff.init.flatten().asVerilog())
            append(";")
            newLine()
        }
    }

    private fun StringBuilder.addSigs() {
        context.types.sigs.values.forEach { sig ->
            val dynamicExpr = context.types.signalDynamicExprs[sig]
            if (dynamicExpr != null) {
                append("wire ")
            } else {
                append("reg ")
            }
            if (sig.signed)
                append("signed ")
            val bitCount = sig.width.bitCount ?: error("Sig \"${sig.name}\" has an undefined width!")
            if (bitCount > 1) {
                append("[")
                append(bitCount - 1)
                append(":0] ")
            }
            append(sig.verilogName)

            if (dynamicExpr != null) {
                append(" = ")
                append(dynamicExpr.expr.verilog)
            }

            append(";")
            newLine()
        }
    }

    private fun StringBuilder.addSequentialBlocks() {
        val clkGroupedDffs = mutableMapOf<String, MutableSet<Dff>>()
        context.types.dffs.values.forEach { dff ->
            clkGroupedDffs.getOrPut(dff.clk.expr.text) { mutableSetOf() }.add(dff)
        }
        clkGroupedDffs.forEach { (_, clkDffs) ->
            newLine()
            append("always @(posedge (")
            append(clkDffs.first().clk.expr.verilog)
            append(")) begin")
            tabCount++
            newLine()

            val rstGroupedDffs = mutableMapOf<String, MutableSet<Dff>>()
            clkDffs.forEach { dff ->
                if (dff.rst == null) {
                    append(dff.q.verilogName)
                    append(" <= ")
                    append(dff.d.verilogName)
                    append(";")
                    newLine()
                } else {
                    rstGroupedDffs.getOrPut(dff.rst.expr.text) { mutableSetOf() }.add(dff)
                }
            }

            rstGroupedDffs.forEach { (_, dffs) ->
                append("if ((")
                append(dffs.first().rst!!.expr.verilog)
                append(") == 1b'1) begin")
                tabCount++
                dffs.forEach {
                    newLine()
                    append(it.q.verilogName)
                    append(" <= ")
                    append(it.init.flatten().asVerilog() ?: error("Failed to get verilog for dff init value!"))
                    append(";")
                }
                tabCount--
                newLine()
                append("end else begin")
                tabCount++
                dffs.forEach {
                    newLine()
                    append(it.q.verilogName)
                    append(" <= ")
                    append(it.d.verilogName)
                    append(";")
                }
                tabCount--
                newLine()
                append("end")
            }
            tabCount--
            newLine()
            append("end")
        }
    }

    private fun StringBuilder.addModuleInstances() {
        context.types.moduleInstances.values.forEach { instance ->
            instance.external.values.forEach { port ->
                when (port.direction) {
                    SignalDirection.Read -> append("wire ") // output
                    SignalDirection.Write -> append("reg ") // input
                    SignalDirection.Both -> error("Direct use of inout is not allowed!")
                }
                if (port.signed)
                    append("signed ")

                val bitCount = port.width.bitCount ?: 1
                if (port.width is ArrayWidth || bitCount > 1) {
                    append("[")
                    append(bitCount - 1)
                    append(":0] ")
                }

                append(port.verilogName)

                append(";")
                newLine()
            }

            // The inputs of an array can be an expression, but we need to index
            // the individual bits for each module. We first must assign the
            // expression to a wire then use this later to get the sub-signal
            if (instance is ModuleInstanceArray) {
                // we can look at any of the instances, so just look at the first
                instance.getAllInstances().first().connections.forEach { (port, connection) ->
                    if (connection is SubSignal) { // only happens when this is selected per instance
                        val sig = connection.getSignal()
                        append("wire ")
                        if (sig.signed)
                            append("signed ")
                        append("[")
                        append((sig.width.bitCount ?: 1) - 1)
                        append(":0] M_")
                        append(instance.name)
                        append("_")
                        append(port)
                        append(" = ")
                        append(sig.verilogName)
                        append(";")
                        newLine()
                    }
                }
            }

            val dimensions = (instance as? ModuleInstanceArray)?.dimensions

            fun addInstModule(module: ModuleInstance, dim: List<Int>?) {
                append(module.parameterizedModuleName)
                append(" ")
                append(module.name.sanitize())
                dim?.forEach {
                    append("_")
                    append(it)
                }
                // TODO: Add parameters for Verilog modules
                append(" (")
                tabCount++
                (module.connections + module.external).asIterable()
                    .forEachIndexed { index, (portName, signalOrSubSignal) ->
                        val port = module.ports[portName] ?: error("Missing port for module instance!")
                        if (index != 0)
                            append(",")
                        newLine()
                        append(".P_")
                        append(port.name)
                        append("(")
                        when (signalOrSubSignal) {
                            is Signal -> append(signalOrSubSignal.verilogName)
                            is SubSignal -> {
                                when (signalOrSubSignal.parent.parent) {
                                    is DynamicExpr -> {
                                        append("M_")
                                        append(instance.name)
                                        append("_")
                                        append(portName)
                                    }

                                    else -> {
                                        append(signalOrSubSignal.verilogName)
                                    }
                                }

                            }
                        }
                        //append(signalOrSubSignal.verilogName)
                        if (module.external.contains(portName) && dim != null && dimensions != null) {
                            var offset = 0
                            val bitCount = signalOrSubSignal.width.bitCount ?: 1
                            dim.indices.forEach { idx ->
                                val d = dim[idx]
                                val w = dimensions.subList(idx + 1, dimensions.size).fold(1) { r, dim ->
                                    r * dim
                                }
                                offset += d * w * bitCount
                            }
                            offset += bitCount - 1
                            append("[")
                            append(offset)
                            if (bitCount > 1) {
                                append("-:")
                                append(bitCount)
                            }
                            append("]")
                        }
                        if (signalOrSubSignal is SubSignal) {
                            append(signalOrSubSignal.toVerilog().selection)
                        }
                        append(")")
                    }

                tabCount--
                newLine()
                append(");")
                newLine()
            }

            when (instance) {
                is ModuleInstance -> addInstModule(instance, null)
                is ModuleInstanceArray -> instance.modules.forEachIndexed { ints, moduleInstance ->
                    addInstModule(
                        moduleInstance,
                        ints
                    )
                }
            }
        }
    }

    override fun enterModule(ctx: LucidParser.ModuleContext) {
        globals.clear()
        tabCount++
    }

    override fun exitModule(ctx: LucidParser.ModuleContext) {
        val instance = (context.instance as? ModuleInstance) ?: error(
            ctx,
            "Verilog converter can only be used with ModuleInstances!"
        )
        ctx.verilog = buildString {
            tabCount--
            append(HEADER)
            newLine()
            addParamHeader()
            tabCount++
            append("module ")
            append(instance.parameterizedModuleName)
            addPorts(instance.internal.values)
            newLine()
            addConstants()
            newLine()
            addDffs()
            newLine()
            addSigs()
            newLine()
            addModuleInstances()
            newLine()

            context.blockParser.alwaysBlocks.keys.forEach { context ->
                append(context.verilog)
                newLine()
            }

            addSequentialBlocks()

            tabCount--
            newLine()
            append("endmodule")
        }
    }

    override fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {
        ctx.verilog = buildString {
            append("always @* ")
            append(ctx.block().requireNotNull(ctx).verilog)
        }
    }

    override fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) {
        ctx.verilog = ctx.assignStat().requireNotNull(ctx).verilog
    }

    override fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext) {
        ctx.verilog = ctx.caseStat().requireNotNull(ctx).verilog
    }

    override fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext) {
        ctx.verilog = ctx.ifStat().requireNotNull(ctx).verilog
    }

    override fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) {
        ctx.verilog = ctx.repeatStat().requireNotNull(ctx).verilog
    }

    override fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) {
        ctx.verilog = ctx.function().requireNotNull(ctx).verilog
    }

    override fun exitAssignStat(ctx: LucidParser.AssignStatContext) {
        val assignee = ctx.signal().requireNotNull(ctx).verilog
        val value = ctx.expr().requireNotNull(ctx).verilog
        ctx.verilog = "$assignee = $value;"
    }

    /**
     * The name of this signal to be used in the verilog conversion.
     * This takes the signal's parent into account to prevent naming collisions.
     */
    private val SignalOrSubSignal.verilogName: String
        get() {
            val baseSignal = getSignal()

            return when (val parent = baseSignal.parent) {
                null -> baseSignal.name.sanitize()
                is Dff -> "D_${parent.name}_${baseSignal.name}"
                is EnumType -> "E_${parent.name}_${baseSignal.name}"
                is GlobalNamespace -> "G_${parent.name}_${baseSignal.name}"
                is ModuleInstanceOrArray -> {
                    val instance = context.instance as? ModuleInstance
                    if (instance == parent)
                        "P_${baseSignal.name}"
                    else
                        "M_${parent.name}_${baseSignal.name}"
                }

                is RepeatSignal -> "R_${parent.name}_${baseSignal.name}\""
                is DynamicExpr -> parent.expr.verilog
            }
        }

    private data class SubSignalData(
        val signed: Boolean,
        val name: String,
        val selection: String
    )

    private fun SubSignal.toVerilog(): SubSignalData {
        var signed = getSignal().signed
        val selection = buildString {
            var currentWidth: SignalWidth? = parent.width
            check(currentWidth?.isDefined() == true) { "Signal width was not defined!" }

            var selectedBitCount = currentWidth!!.bitCount!! // the defined check above makes this safe

            var first = true

            if (selection.isNotEmpty())
                append("[")

            for (selector in selection) {
                if (!first)
                    append("+")
                else
                    first = false

                when (currentWidth) {
                    is ArrayWidth, is SimpleWidth -> {
                        val s = selector as? SignalSelector.Bits ?: error("Struct selector used on an array!")

                        append("(")
                        when (s.context) {
                            is SelectionContext.Constant -> append(s.range.first)
                            is SelectionContext.Single -> append(s.context.bit.verilog)
                            is SelectionContext.Fixed -> append(s.context.start.verilog)
                            is SelectionContext.DownTo -> append("(${s.context.stop.verilog})-${s.context.width - 1}")
                            is SelectionContext.UpTo -> append(s.context.start)
                        }
                        append(")")

                        val elementSize = if (currentWidth is ArrayWidth) currentWidth.next.bitCount!! else 1

                        // scale the offset by the size of each element
                        if (elementSize > 1) {
                            append("*")
                            append(elementSize)
                        }

                        signed = false
                        selectedBitCount = s.count * elementSize
                        currentWidth = if (currentWidth is ArrayWidth)
                            when (s.context) {
                                is SelectionContext.Constant,
                                is SelectionContext.Single ->
                                    currentWidth.next

                                is SelectionContext.Fixed,
                                is SelectionContext.DownTo,
                                is SelectionContext.UpTo ->
                                    null // these select a range of bits and can't be selected further
                            } else null
                    }

                    is StructWidth -> {
                        val s = selector as? SignalSelector.Struct ?: error("Bit selector used on a struct!")
                        val type = currentWidth.type
                        val member = type[s.member] ?: error("Struct member could not be found!")

                        append(type.offsetOf(s.member))

                        signed = member.signed
                        currentWidth = member.width
                        selectedBitCount = currentWidth.bitCount!!
                    }

                    UndefinedSimpleWidth -> error("Undefined width during verilog conversion!")
                    null -> error("Too many selectors for the signal width!")
                }
            }

            if (selection.isNotEmpty()) {
                if (selectedBitCount > 1) {
                    append("+")
                    append(selectedBitCount - 1)
                    append("-:")
                    append(selectedBitCount)
                    append("]")
                } else {
                    append("]")
                }
            }
        }
        return SubSignalData(signed, parent.verilogName, selection)
    }

    override fun exitSignal(ctx: LucidParser.SignalContext) {
        val signal = context.resolve(ctx) ?: error(ctx, "Failed to resolve signal for \"${ctx.text}\"")

        val baseSignal = signal.getSignal()

        if (baseSignal.parent is GlobalNamespace)
            globals.add(baseSignal)

        val verilogName = signal.verilogName


        // build the bit selection string (if sub-signal)
        val selection = (signal as? SubSignal)?.toVerilog()
        val signed = selection?.signed ?: baseSignal.signed

        val sigVerilog = "$verilogName${selection?.selection ?: ""}"
        val write = ctx.parent is LucidParser.AssignStatContext
        ctx.verilog = if (signed && !write) "\$signed($sigVerilog)" else sigVerilog
    }

    override fun enterBlock(ctx: LucidParser.BlockContext) {
        tabCount++
    }

    override fun exitBlock(ctx: LucidParser.BlockContext) {
        ctx.verilog = buildString {
            append("begin")
            newLine()

            // add default assignments for all driven DFFs if this is an always block
            if (ctx.parent is LucidParser.AlwaysBlockContext) {
                val drivenSignals =
                    context.signalDriver.getDrivenSignals(ctx) ?: error(ctx, "Missing driven signals for block!")
                val dffs = drivenSignals.mapNotNull { it.parent as? Dff }
                dffs.forEach { dff ->
                    append(dff.d.verilogName)
                    append(" = ")
                    append(dff.q.verilogName)
                    append(";")
                    newLine()
                }
                if (dffs.isNotEmpty())
                    newLine()
            }

            ctx.alwaysStat().forEachIndexed { index, alwaysStatContext ->
                if (index > 0)
                    newLine()
                append(alwaysStatContext.verilog)
            }

            tabCount--
            newLine()
            append("end")
        }
    }

    override fun enterCaseStat(ctx: LucidParser.CaseStatContext) {
        tabCount++
    }

    override fun exitCaseStat(ctx: LucidParser.CaseStatContext) {
        ctx.verilog = buildString {
            tabCount--
            newLine()
            tabCount++
            append("case (")
            append(ctx.expr().requireNotNull(ctx).verilog)
            append(")")
            ctx.caseElem().forEach {
                newLine()
                append(it.verilog)
            }
            tabCount--
            newLine()
            append("endcase")
        }

    }

    override fun enterCaseElem(ctx: LucidParser.CaseElemContext) {
        tabCount++
    }

    override fun exitCaseElem(ctx: LucidParser.CaseElemContext) {
        ctx.verilog = buildString {
            append(ctx.expr()?.verilog ?: "default")
            append(": begin")
            ctx.caseBlock()?.alwaysStat()?.forEach {
                newLine()
                append(it.verilog)
            }
            tabCount--
            newLine()
            append("end")
        }
    }

    override fun exitIfStat(ctx: LucidParser.IfStatContext) {
        ctx.verilog = buildString {
            append("if (")
            append(ctx.expr().requireNotNull(ctx).verilog)
            append(") ")
            append(ctx.block().requireNotNull(ctx).verilog)
            ctx.elseStat()?.let {
                append(" ")
                append(it.verilog)
            }
        }
    }

    override fun exitElseStat(ctx: LucidParser.ElseStatContext) {
        ctx.verilog = buildString {
            append("else")
            append(ctx.block().requireNotNull(ctx).verilog)
        }
    }

    override fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) {
        val repeatSignal =
            context.blockParser.repeatSignals[ctx] ?: error(ctx, "Missing repeat signal for repeat block!")
        val repValue = context.resolve(ctx.expr().requireNotNull(ctx)) as? SimpleValue ?: error(
            ctx,
            "Missing repeat count for repeat block!"
        )
        ctx.verilog = buildString {
            val repSigName = repeatSignal.verilogName
            append("for (")
            append(repSigName)
            append(" = 0; ")
            append(repSigName)
            append(" < ")
            append(repValue.asVerilog() ?: error(ctx, "Repeat value \"$repValue\" could not be converted to verilog!"))
            append("; ")
            append(repSigName)
            append(" = ")
            append(repSigName)
            append(" + 1) ")
            append(ctx.repeatBlock().requireNotNull(ctx).verilog)
        }
    }

    override fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext) {
        ctx.verilog = ctx.block().requireNotNull(ctx).verilog
    }

    override fun exitExprTernary(ctx: LucidParser.ExprTernaryContext) {
        if (handleConstant(ctx))
            return

        ctx.verilog = buildString {
            append(ctx.expr(0).requireNotNull(ctx).verilog)
            append(" ? ")
            append(ctx.expr(1).requireNotNull(ctx).verilog)
            append(" : ")
            append(ctx.expr(2).requireNotNull(ctx).verilog)
        }
    }

    override fun exitExprNum(ctx: LucidParser.ExprNumContext) {
        if (!handleConstant(ctx))
            error(ctx, "Missing value for number: \"${ctx.text}\"")
    }

    override fun exitExprConcat(ctx: LucidParser.ExprConcatContext) {
        if (handleConstant(ctx))
            return
        verilog[ctx] = ctx.expr().joinToString(", ", "{", "}") {
            verilog[it] ?: error(it, "Missing verilog for expr in concat: \"${it.text}\"")
        }
    }

    override fun exitExprReduction(ctx: LucidParser.ExprReductionContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = buildString {
            append("(")
            append(ctx.getChild(0).requireNotNull(ctx).text)
            append(ctx.expr().requireNotNull(ctx).verilog)
            append(")")
        }
    }

    override fun exitExprInvert(ctx: LucidParser.ExprInvertContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = buildString {
            append(ctx.getChild(0).requireNotNull(ctx).text)
            append(ctx.expr().requireNotNull(ctx).verilog)
        }
    }

    override fun exitExprStruct(ctx: LucidParser.ExprStructContext) {
        if (handleConstant(ctx))
            error(ctx, "Missing value for struct constant: \"${ctx.text}\"")
    }

    override fun exitExprArray(ctx: LucidParser.ExprArrayContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = buildString {
            append("{")
            ctx.expr().forEachIndexed { index, exprContext ->
                if (index > 0)
                    append(", ")
                append(exprContext.verilog)
            }
            append("}")
        }
    }

    private fun basic2OpExpr(ctx: LucidParser.ExprContext, expr: List<LucidParser.ExprContext>) {
        if (handleConstant(ctx))
            return
        ctx.verilog = buildString {
            append(expr[0].verilog)
            append(" ")
            append(ExprParser.getOperator(ctx) ?: error(ctx, "Missing operator!"))
            append(" ")
            append(expr[1].verilog)
        }
    }

    override fun exitExprShift(ctx: LucidParser.ExprShiftContext) {
        basic2OpExpr(ctx, ctx.expr())
    }

    override fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext) {
        basic2OpExpr(ctx, ctx.expr())
    }

    override fun exitExprLogical(ctx: LucidParser.ExprLogicalContext) {
        basic2OpExpr(ctx, ctx.expr())
    }

    override fun exitExprNegate(ctx: LucidParser.ExprNegateContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = "-${ctx.expr().requireNotNull(ctx).verilog}"
    }

    override fun exitExprGroup(ctx: LucidParser.ExprGroupContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = "(${ctx.expr().requireNotNull(ctx).verilog})"
    }

    override fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext) {
        basic2OpExpr(ctx, ctx.expr())
    }

    override fun exitExprFunction(ctx: LucidParser.ExprFunctionContext) {
        if (handleConstant(ctx))
            return
        error(ctx, "Not yet implemented") // TODO
    }

    override fun exitExprCompare(ctx: LucidParser.ExprCompareContext) {
        basic2OpExpr(ctx, ctx.expr())
    }

    override fun exitExprDup(ctx: LucidParser.ExprDupContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = buildString {
            append("{")
            append(ctx.expr(0).requireNotNull(ctx).verilog)
            append("{")
            append(ctx.expr(1).requireNotNull(ctx).verilog)
            append("}}")
        }
    }

    override fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext) {
        if (handleConstant(ctx))
            return
        basic2OpExpr(ctx, ctx.expr())
    }

    override fun exitExprSignal(ctx: LucidParser.ExprSignalContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = ctx.signal().requireNotNull(ctx).verilog
    }

    companion object {
        /**
         * A list of Verilog's reserved words.
         */
        private val reservedWords = listOf(
            "always",
            "assign",
            "automatic",
            "begin",
            "case",
            "casex",
            "casez",
            "cell",
            "config",
            "deassign",
            "default",
            "defparam",
            "design",
            "disable",
            "edge",
            "else",
            "end",
            "endcase",
            "endconfig",
            "endfunction",
            "endgenerate",
            "endmodule",
            "endprimitive",
            "endspecify",
            "endtable",
            "endtask",
            "event",
            "for",
            "force",
            "foreve",
            "fork",
            "function",
            "generate",
            "genvar",
            "if",
            "ifnone",
            "incdir",
            "include",
            "initial",
            "inout",
            "instance",
            "join",
            "liblist",
            "library",
            "localparam",
            "macromodule",
            "module",
            "negedge",
            "noshowcancelled",
            "output",
            "parameter",
            "posedge",
            "primitive",
            "pulsestyle_ondetect",
            "pulsestyle_onevent",
            "reg",
            "release",
            "repeat",
            "scalared",
            "showcancelled",
            "signed",
            "specify",
            "specparam",
            "strength",
            "table",
            "task",
            "tri",
            "tri0",
            "tri1",
            "triand",
            "wand",
            "trior",
            "wor",
            "trireg",
            "unsigned",
            "use",
            "vectored",
            "wait",
            "while",
            "wire",
        )

        /**
         * Returns a modified version of the string if it matches a word in [reservedWords].
         * If it doesn't match, it returns the same string as is.
         */
        private fun String.sanitize(): String =
            if (reservedWords.contains(this))
                "L_$this"
            else
                this

        private val HEADER: String = """
            /*
                This file was generated automatically by Alchitry Labs ${Env.version}.
                Do not edit this file directly. Instead edit the original Lucid source.
                This is a temporary file and any changes made to it will be destroyed.
            */
        """.trimIndent()

    }
}