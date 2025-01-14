package com.alchitry.labs2.parsers.hdl.lucid

import com.alchitry.labs2.Env
import com.alchitry.labs2.asSingleLine
import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidListener
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.LucidParser.FunctionContext
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.lucid.parsers.ArraySize
import com.alchitry.labs2.parsers.hdl.lucid.parsers.ExprParser
import com.alchitry.labs2.parsers.hdl.lucid.parsers.firstParentOrNull
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.Function
import com.alchitry.labs2.parsers.hdl.values.*
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationType
import kotlinx.coroutines.runBlocking
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree
import kotlin.math.absoluteValue

sealed interface VerilogConverter : LucidListener {
    val verilog: Map<ParseTree, String>
}

class SystemVerilogConverter(
    private val context: LucidBlockContext
) : LucidBaseListener(), VerilogConverter {
    override val verilog = mutableMapOf<ParseTree, String>()
    private var tabCount: Int = 0

    /**
     * Throws an [IllegalStateException] with the provided message and line/offset of the provided context.
     */
    private fun error(ctx: ParserRuleContext, message: String): Nothing {
        val notation = Notation.from(ctx, message, NotationType.Error)
        error(notation.toString())
    }

    /**
     * Adds a new line and indents the current [tabCount]
     */
    private fun StringBuilder.newLine(): StringBuilder {
        append("\n")
        for (i in 0..<tabCount)
            append("    ")
        return this
    }

    /**
     * Checks if the expression is constant and can be replaced by a simple value.
     * @return true if the expression was handled, false otherwise
     */
    private fun handleConstant(ctx: LucidParser.ExprContext): Boolean {
        val value = context.resolve(ctx) ?: return false
        if (value.type == ExprType.Constant) {
            ctx.verilog = try {
                value.value.toVerilog()
            } catch (e: Exception) {
                error(ctx, "$value could not be converted to verilog: ${e.message}")
            }
            return true
        }
        return false
    }

    /**
     * Convenient getter/setter for the internal verilog map.
     * It throws an [IllegalStateException] if the verilog is missing for the [ParseTree].
     */
    private var ParseTree.verilog: String
        get() = this@SystemVerilogConverter.verilog[this]
            ?: error("Missing verilog for ${this::class.simpleName}: \"${this.text.asSingleLine()}\"")
        set(value) {
            this@SystemVerilogConverter.verilog[this] = value
        }

    /**
     * Throws an [IllegalStateException] if the value is null with a message containing the location of [context].
     * @param context The context to use as the error location if this is null.
     */
    private inline fun <reified T : ParseTree> T?.requireNotNull(context: ParserRuleContext): T {
        return this ?: error(
            context,
            "Context \"${T::class.simpleName}\" in ${this@SystemVerilogConverter.context.sourceFile.name} was null!"
        )
    }

    private fun SignalWidth.verilogArrayWidths(): String {
        return when (this) {
            is BitWidth -> ""
            is DefinedArrayWidth -> "[${size - 1}:0]${next.verilogArrayWidths()}"
            is DefinedSimpleWidth -> "[${size - 1}:0]"
            is StructWidth -> ""
            is ResolvableSimpleWidth<*> -> "[(${context.verilog})-1:0]"
            is ResolvableArrayWidth<*> -> "[(${context.verilog})-1:0]${next.verilogArrayWidths()}"
            is UndefinedWidth -> error("Undefined width during verilog conversion!")
        }
    }

    private fun StringBuilder.addParams(params: Collection<Parameter>) {
        if (params.isEmpty())
            return

        append(" #(")
        tabCount++

        params.forEachIndexed { index, param ->
            newLine()
            append("parameter ")
            append(param.name)
            append(" = ")
            if (param.default != null) {
                append(param.default.toVerilog())
            } else {
                append("0")
            }

            if (index != params.size - 1)
                append(",")
        }

        tabCount--
        newLine()
        append(")")
    }

    private fun StringBuilder.addPorts(ports: Collection<Signal>) {
        append(" (")
        tabCount++
        ports.forEachIndexed { index, port ->
            newLine()
            val type = port.width.firstStructType()
            when (port.direction) {
                SignalDirection.Read -> append("input ${type ?: "wire"} ")
                SignalDirection.Write -> append("output ${type ?: "reg"} ")
                SignalDirection.Both -> append("inout ${type ?: "wire"} ")
            }
            if (port.signed)
                append("signed ")
            append(port.width.verilogArrayWidths().appendSpaceIfNotBlank())
            append(port.verilogName)
            if (index != ports.size - 1)
                append(",")
        }
        tabCount--
        newLine()
        append(");")
    }

    private fun StringBuilder.addIoSignals(ports: Collection<Signal>) {
        ports.forEach { port ->
            if (port.direction != SignalDirection.Both)
                return@forEach

            if (context.boundInouts.contains(port.name))
                return@forEach

            val type = port.width.firstStructType() ?: "logic"
            append("$type ")
            if (port.signed)
                append("signed ")

            val name = port.verilogName
            append(port.width.verilogArrayWidths().appendSpaceIfNotBlank())
            append("IO_")
            append(name)
            append(";")
            newLine()

            append("assign ")
            append(name)
            append(" = IO_")
            append(name)
            append(";")

            newLine()
        }
    }

    private fun StringBuilder.addConstants() {
        val constants = mutableListOf<Signal>().apply {
            addAll(context.constant.localConstants.values.filter { it.type == ExprType.Constant })
            context.enum.localEnumType.values.forEach {
                addAll(it.memberSignals.values)
            }
        }

        context.constant.localConstants.filter { it.value.type == ExprType.Fixed }.forEach { (name, signal) ->
            append("localparam ")

            if (!signal.width.isSimple()) {
                append(signal.width.firstStructType()?.appendSpaceIfNotBlank() ?: "logic ")
                append(signal.width.verilogArrayWidths().appendSpaceIfNotBlank())
            }

            append(signal.verilogName)
            append(" = ")
            append(
                context.constant.getContext(name)?.expr()?.verilog
                    ?: error("Failed to resolve verilog for constant expression!")
            )
            append(";")
            newLine()
        }

        constants.forEach {
            check(it.type == ExprType.Constant) { "Constant value wasn't marked as constant!" }
            append("localparam ")

            if (!it.width.isSimple()) {
                append(it.width.firstStructType()?.appendSpaceIfNotBlank() ?: "logic ")
                append(it.width.verilogArrayWidths().appendSpaceIfNotBlank())
            }

            append(it.verilogName)
            append(" = ")
            append(it.initialValue.toVerilog())
            append(";")
            newLine()
        }
    }

    private fun StringBuilder.addDffs() {
        context.types.dffs.values.forEach { dff ->
            val type = dff.d.width.firstStructType() ?: "logic"
            append("$type ")
            if (dff.signed)
                append("signed ")
            append(dff.d.width.verilogArrayWidths().appendSpaceIfNotBlank())
            append(dff.d.verilogName)
            append(", ")
            append(dff.q.verilogName)
            append(" = ")
            append(dff.initContext?.verilog ?: "0")
            append(";")
            newLine()
        }
    }

    private fun StringBuilder.addSigs() {
        (context.types.sigs.values +
                context.blockParser.repeatSignals.values +
                context.types.localSignals.flatMap { scope -> scope.value.map { it.value.signal } }).forEach { sig ->
            val dynamicExpr = context.types.signalDynamicExprs[sig]
            val type = sig.width.firstStructType() ?: "logic"
            append("$type ")
            if (sig.signed)
                append("signed ")
            append(sig.width.verilogArrayWidths().appendSpaceIfNotBlank())
            append(sig.verilogName)

            if (dynamicExpr != null) {
                append(" = ")
                append(dynamicExpr.expr.verilog)
            }

            append(";")
            val parent = sig.parent
            if (parent is RepeatSignal) {
                newLine()
                append("$type ")
                if (sig.signed)
                    append("signed ")
                val width = BitListWidth(parent.block.minIndexBits)
                append(width.verilogArrayWidths().appendSpaceIfNotBlank())
                append("R${sig.verilogName}")
                append(";")
            }

            newLine()
        }

    }

    private fun StringBuilder.addSequentialBlocks() {
        val groupedDffs = mutableMapOf<Pair<String, String?>, MutableSet<Dff>>()
        context.types.dffs.values.forEach { dff ->
            groupedDffs.getOrPut(dff.clk.expr.text to dff.rst?.expr?.text) { mutableSetOf() }.add(dff)
        }

        groupedDffs.forEach { (_, dffGroup) ->
            val asyncReset = dffGroup.first().asyncReset
            newLine()
            append("always @(posedge (")
            append(dffGroup.first().clk.expr.verilog)
            if (asyncReset) {
                append(") or posedge (")
                append(dffGroup.first().rst!!.expr.verilog)
            }
            append(")) begin")
            tabCount++
            newLine()

            val rstGroupedDffs = mutableMapOf<String, MutableSet<Dff>>()
            dffGroup.forEach { dff ->
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
                append(") == 1'b1) begin")
                tabCount++
                dffs.forEach {
                    newLine()
                    append(it.q.verilogName)
                    append(" <= ")
                    append(it.initContext?.verilog ?: "0")
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
            val parameterNameMap = mutableMapOf<String, String>()

            fun addLocalParam(name: String, getValue: () -> String) {
                val paramValue = instance.paramAssignments[name]
                val newName = "_MP_${name}_${instance.hashCode()}"
                parameterNameMap[name] = newName
                append("localparam ")
                if (paramValue != null) {
                    val width = paramValue.dynamicExpr.value.width
                    if (!width.isSimple()) {
                        append("logic ")
                        width.firstStructType()?.let { append(it.appendSpaceIfNotBlank()) }
                        append(width.verilogArrayWidths().appendSpaceIfNotBlank())
                    }
                }
                append(newName)
                append(" = ")
                if (paramValue != null) {
                    append(paramValue.dynamicExpr.expr.verilog)
                } else {
                    append(getValue())
                }
                append(";")
                newLine()
            }

            when (val moduleContext = instance.module.context) {
                is LucidParser.ModuleContext -> {
                    val localParameters = mutableMapOf<String, Signal>()
                    moduleContext.paramList()?.paramDec()?.forEach inner@{ paramDecContext ->
                        val name = paramDecContext.name()?.text ?: return@inner
                        val defaultWidth = instance.module.parameters[name]?.default?.width
                        Signal(
                            name,
                            SignalDirection.Read,
                            instance,
                            UndefinedValue(defaultWidth ?: UndefinedSimpleWidth()),
                            ExprType.Fixed
                        ).also {
                            localParameters[name] = it
                            context.localSignals[name] = it
                        }
                        addLocalParam(name) {
                            val valueCtx = paramDecContext.paramDefault()?.expr()
                                ?: error("Parameter value wasn't specified and it has no default!")
                            runBlocking { context.walk(valueCtx) }
                            valueCtx.verilog
                        }
                    }

                    fun resolveAll(width: SignalWidth) {
                        when (width) {
                            is ResolvableArrayWidth<*> -> {
                                runBlocking { context.walk(width.context) }
                                resolveAll(width.next)
                            }

                            is ResolvableSimpleWidth<*> -> runBlocking { context.walk(width.context) }
                            is SimpleWidth -> return
                            is StructWidth -> width.type.values.forEach { resolveAll(it.width) }
                            is ArrayWidth -> resolveAll(width.next)
                        }
                    }

                    instance.external.values.forEach { signal ->
                        resolveAll(signal.width)
                    }
                }

                is VerilogParser.Module_declarationContext -> {
                    moduleContext.module_parameter_port_list()?.parameter_declaration()
                        ?.flatMap { it.list_of_param_assignments()?.param_assignment() ?: emptyList() }
                        ?.forEach inner@{ paramAssignmentContext ->
                            val name = paramAssignmentContext.parameter_identifier()?.text ?: return@inner
                            addLocalParam(name) {
                                var value =
                                    paramAssignmentContext.constant_mintypmax_expression()?.constant_expression(0)?.text
                                        ?: error("Failed to get text for parameter expression!")
                                parameterNameMap.forEach { (name, newName) -> value = value.replace(name, newName) }
                                value
                            }
                        }

                    instance.external.values.forEach { signal ->
                        val width = signal.width
                        if (width is ResolvableWidth<*>) {
                            var msb = (width.context as VerilogParser.Range_Context).msb_constant_expression()!!.text
                            var lsb = (width.context as VerilogParser.Range_Context).lsb_constant_expression()!!.text
                            parameterNameMap.forEach { (name, newName) ->
                                msb = msb.replace(name, newName)
                                lsb = lsb.replace(name, newName)
                            }
                            verilog[width.context] = "(($msb) - ($lsb) + 1)"
                        }
                    }
                }

                else -> error("Module context wasn't a Lucid or Verilog module! Found: ${moduleContext.javaClass.canonicalName}")
            }

            parameterNameMap.keys.forEach { context.localSignals.remove(it) }

            instance.external.values.forEach { port ->
                append(port.width.firstStructType() ?: "logic")
                append(" ")
                if (port.signed)
                    append("signed ")

                append(port.width.verilogArrayWidths().appendSpaceIfNotBlank())
                append(port.verilogName)

                append(";")
                newLine()
            }

            // The inputs of an array can be an expression, but we need to index
            // the individual bits for each module. We first must assign the
            // expression to a wire then use this later to get the sub-signal
            if (instance is ModuleInstanceArray) {
                // we can look at any of the instances, so just look at the first
                instance.modules.first().connections.forEach { (port, connection) ->
                    if (connection is SubSignal) { // only happens when this is selected per instance
                        val sig = connection.getSignal()
                        append(sig.width.firstStructType() ?: "logic")
                        append(" ")
                        if (sig.signed)
                            append("signed ")
                        append(sig.width.verilogArrayWidths().appendSpaceIfNotBlank())
                        append("M_${instance.name}_$port")
                        append(" = ")
                        append(sig.verilogName)
                        append(";")
                        newLine()
                    }
                }
            }

            fun addInstModule(module: ModuleInstance, dim: List<String>?) {
                append(module.module.name)

                if (module.module.parameters.isNotEmpty()) {
                    append(" #(")
                    tabCount++

                    module.parameters.values.forEachIndexed { index, p ->
                        val param = module.module.parameters[p.name] ?: error("Failed to find parameter \"${p.name}\"!")
                        if (index != 0)
                            append(",")
                        newLine()
                        append(".")
                        append(param.name)
                        append("(")
                        append(parameterNameMap[param.name]!!)
                        if (instance.paramAssignments[param.name]?.local == true)
                            dim?.forEach { append("[$it]") }
                        append(")")
                    }

                    tabCount--
                    newLine()
                    append(") ")
                } else {
                    append(" ")
                }

                append(module.name.sanitize())
                append(" (")
                tabCount++
                (module.connections + module.external).asIterable()
                    .forEachIndexed { index, (portName, signalOrSubSignal) ->
                        val port = module.ports[portName] ?: error("Missing port for module instance!")
                        if (index != 0)
                            append(",")
                        newLine()
                        append(".")
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
                        if (module.external.contains(portName) || signalOrSubSignal is SubSignal) {
                            dim?.forEach { append("[$it]") }
                        }
                        append(")")
                    }

                tabCount--
                newLine()
                append(");")
            }

            newLine()

            when (instance) {
                is ModuleInstance -> addInstModule(instance, null)
                is ModuleInstanceArray -> {
                    val dimIndexNames = instance.dimensions.indices.map { index ->
                        "idx_${index}_${instance.hashCode()}"
                    }
                    append("genvar ")
                    instance.dimensions.forEachIndexed { index, arraySize ->
                        if (index != 0)
                            append(", ")
                        append(dimIndexNames[index])
                    }
                    append(";")
                    newLine()
                    newLine()
                    append("generate")
                    tabCount++
                    newLine()
                    instance.dimensions.forEachIndexed { index, arraySize ->
                        val idxName = dimIndexNames[index]
                        append("for ($idxName = 0; $idxName < ")
                        when (arraySize) {
                            is ArraySize.Fixed -> append(arraySize.size)
                            is ArraySize.Resolvable -> append(arraySize.context.verilog)
                        }
                        append("; $idxName = $idxName + 1) begin: forLoop_$idxName")
                        tabCount++
                        newLine()
                    }
                    addInstModule(
                        instance.modules.first(),
                        dimIndexNames
                    )
                    instance.dimensions.forEach { _ ->
                        tabCount--
                        newLine()
                        append("end")
                    }
                    tabCount--
                    newLine()
                    append("endgenerate")
                }
            }
            newLine()
            newLine()
        }
    }

    override fun enterModule(ctx: LucidParser.ModuleContext) {
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
            newLine()
            tabCount++
            append("module ")
            append(instance.module.name)
            addParams(instance.module.parameters.values)
            addPorts(instance.internal.values)
            newLine()
            addConstants()
            newLine()
            addDffs()
            newLine()
            addSigs()
            addIoSignals(instance.internal.values)
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

    override fun exitAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) {
        val expr = ctx.sigDec()?.expr()
        if (expr == null) {
            ctx.verilog = "" // no default value
            return
        }
        val signal =
            context.resolveSignal(ctx, ctx.sigDec()?.name()?.text ?: error("Missing name for local signal!")) as? Signal
                ?: error("Failed to resolve local signal: \"${ctx.text.asSingleLine()}\"")
        ctx.verilog = "${signal.verilogName} = ${expr.verilog};"
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
                is GlobalNamespace -> "LucidGlobals::G_${parent.name}_${baseSignal.name}"
                is ModuleInstanceOrArray -> {
                    val instance = context.instance as? ModuleInstance
                    when {
                        instance == parent -> baseSignal.name // this is a local signal
                        baseSignal.type == ExprType.Fixed -> "_MP_${baseSignal.name}_${parent.hashCode()}" // instance parameter
                        else -> "M_${parent.name}_${baseSignal.name}"
                    }
                }

                is RepeatSignal -> "R_${parent.name}_${baseSignal.name}"
                is LocalSignal -> "L_${parent.name}_${baseSignal.name}"
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
            checkNotNull(currentWidth) { "Signal width was not defined!" }


            for (selector in selection) {
                when (currentWidth) {
                    is DefinedArrayWidth, is DefinedSimpleWidth, is ResolvableWidth<*> -> {
                        val s = selector as? SignalSelector.Bits ?: error("Struct selector used on an array!")

                        val widthString = when (currentWidth) {
                            is DefinedArrayWidth -> currentWidth.size.toString()
                            is DefinedSimpleWidth -> currentWidth.size.toString()
                            is ResolvableWidth<*> -> currentWidth.context.verilog
                            else -> error("Impossible width type!")
                        }

                        fun LucidParser.ExprContext.absString(): String {
                            val expr = context.resolve(this)
                            val value = (expr?.value as? SimpleValue)
                            val curWidth = currentWidth
                            return if (value?.signed == true) {
                                if (expr.type.fixed && value.isNumber()) {
                                    val v = value.toBigInt()!!.toInt()
                                    val w = when (curWidth) {
                                        is DefinedArrayWidth -> curWidth.size
                                        is DefinedSimpleWidth -> curWidth.size
                                        else -> return if (v < 0) {
                                            "(($widthString) - ${v.absoluteValue})"
                                        } else {
                                            v.toString()
                                        }
                                    }
                                    return (w + v).toString()
                                } else {
                                    val v = this.verilog
                                    "(($v) < 0 ? ($widthString) + ($v) : ($v))"
                                }
                            } else {
                                this.verilog
                            }
                        }

                        append("[")
                        when (s.context) {
                            is SelectionContext.Constant -> append("${s.range.last}:${s.range.first}")
                            is SelectionContext.Single -> append(s.context.bit.absString())
                            is SelectionContext.Fixed -> append("${s.context.stop.absString()}:${s.context.start.absString()}")
                            is SelectionContext.DownTo -> append("${s.context.stop.absString()}-:${s.context.width.verilog}")
                            is SelectionContext.UpTo -> append("${s.context.start.absString()}+:${s.context.width.verilog}")
                        }
                        append("]")

                        signed = false

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

                        append(".${member.name}")

                        signed = member.signed
                        currentWidth = member.width
                    }

                    is UndefinedWidth -> error("Undefined width during verilog conversion!")
                    null -> error("Too many selectors for the signal width!")
                }
            }
        }
        return SubSignalData(
            signed,
            parent.verilogName,
            selection
        )
    }

    private fun SignalOrSubSignal.toVerilog(write: Boolean): String {
        val baseSignal = getSignal()

        val verilogName = verilogName

        // build the bit selection string (if sub-signal)
        val selection = (this as? SubSignal)?.toVerilog()
        val signed = selection?.signed ?: baseSignal.signed

        val writePrefix =
            if (write && getSignal().parent is ModuleInstanceOrArray && direction == SignalDirection.Both) {
                "IO_"
            } else ""

        val sigVerilog = "$writePrefix$verilogName${selection?.selection ?: ""}"

        return if (signed && !write) "\$signed($sigVerilog)" else sigVerilog
    }

    override fun exitSignal(ctx: LucidParser.SignalContext) {
        val signal = context.resolve(ctx)
        if (signal == null) {
            if (ctx.parent is LucidParser.ExprSignalContext && ctx.parent?.parent is LucidParser.RepeatStatContext)
                return

            val functionCtx = ctx.firstParentOrNull { it is FunctionContext }
            if (functionCtx is FunctionContext && functionCtx.FUNCTION_ID()?.text == "$" + Function.WIDTH.label)
                return
            error(ctx, "Failed to resolve signal for \"${ctx.text}\"")
        }

        ctx.verilog = signal.toVerilog(write = ctx.parent is LucidParser.AssignStatContext)
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
            append("else ")
            append(ctx.block().requireNotNull(ctx).verilog)
        }
    }

    override fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) {
        val repeatBlock = context.blockParser.resolveRepeatBlock(ctx) ?: error(ctx, "Missing repeat block!")
        val repeatSignal =
            context.blockParser.repeatSignals[ctx] ?: error(ctx, "Missing repeat signal for repeat block!")
        val startExpr = repeatBlock.startExprCtx?.verilog ?: "0"
        val stepExpr = repeatBlock.stepExprCtx?.verilog ?: "1"
        ctx.verilog = buildString {
            val repSigName = repeatSignal.verilogName
            append("for (R")
            append(repSigName)
            append(" = 0; R")
            append(repSigName)
            append(" < ${repeatBlock.countExprCtx.verilog}; R")
            append(repSigName)
            append(" = R")
            append(repSigName)
            append(" + 1) ")
            val blockLines = ctx.repeatBlock().requireNotNull(ctx).verilog.split("\n").toMutableList()
            blockLines.add(1, buildString {
                for (i in 0..<tabCount + 1)
                    append("  ")
                append(repSigName)
                append(" = (")
                append(startExpr)
                append(") + R")
                append(repSigName)
                append(" * (")
                append(stepExpr)
                append(");")
            })
            append(blockLines.joinToString("\n"))
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
        if (!handleConstant(ctx))
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
        val functionCtx = ctx.function() ?: error(ctx, "Function context missing!")
        val function = context.expr.functions[functionCtx] ?: error(ctx, "Failed to resolve function!")
        ctx.verilog = when (function) {
            Function.FLATTEN,
            Function.BUILD ->
                functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")

            Function.SIGNED ->
                "\$signed(${
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                })"

            Function.UNSIGNED ->
                "\$unsigned(${
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                })"

            Function.POWER -> {
                val p1 =
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                val p2 =
                    functionCtx.functionExpr(1)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                "(64'($p1) ** ($p2))"
            }

            Function.CDIV -> {
                val p1 =
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                val p2 =
                    functionCtx.functionExpr(1)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                "((($p1) - 1) / ($p2) + 1)"
            }

            Function.RESIZE -> {
                val expr =
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                val size =
                    functionCtx.functionExpr(1)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                "($size)'($expr)"
            }

            Function.CLOG2 -> {
                "\$clog2(${
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                })"
            }

            Function.FIXEDPOINT, Function.CFIXEDPOINT, Function.FFIXEDPOINT -> {
                val real =
                    functionCtx.functionExpr(0)?.REAL()?.text ?: functionCtx.functionExpr(0)?.expr()?.verilog ?: error(
                        ctx,
                        "Missing value for ${function.label}!"
                    )
                val width =
                    functionCtx.functionExpr(1)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                val fractional =
                    functionCtx.functionExpr(2)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                when (function) {
                    Function.FIXEDPOINT -> "($width)'(int'(($real) * (2 ** ($fractional))))"
                    Function.CFIXEDPOINT -> "($width)'(int'(\$ceil(($real) * (2 ** ($fractional)))))"
                    Function.FFIXEDPOINT -> "($width)'(\$rtoi(($real) * (2 ** ($fractional))))"
                    else -> error("Impossible case!")
                }
            }

            Function.REVERSE -> {
                val signalContext =
                    (functionCtx.functionExpr(0)?.expr() as? LucidParser.ExprSignalContext)?.signal() ?: error(
                        ctx,
                        "Non-constant \$reverse() used on something other than a signal!"
                    )
                val signal = context.signal.resolve(signalContext)
                    ?: error(ctx, "Failed to resolve signal for ${function.label}!")
                val selection = when (signal) {
                    is Signal -> {
                        val msb = when (val width = signal.width) {
                            is DefinedArrayWidth -> width.size
                            is BitListWidth -> width.size
                            BitWidth -> 1
                            else -> error(ctx, "\$reverse() used on a non-array signal!")
                        } - 1
                        listOf(SignalSelector.Bits(0..msb, SelectionContext.Constant))
                    }

                    is SubSignal -> signal.selection
                }
                val lastSelector = selection.last()
                if (lastSelector !is SignalSelector.Bits)
                    error(ctx, "\$reverse() used on a non-array signal!")

                lastSelector.range.joinToString(separator = ", ", prefix = "{", postfix = "}") { index ->
                    val newSelector = selection.toMutableList().apply {
                        removeLast()
                        add(SignalSelector.Bits(index, SelectionContext.Constant))
                    }
                    val selectedSignal = signal.getSignal().select(newSelector)
                    selectedSignal.toVerilog(write = false)
                }
            }

            Function.WIDTH -> {
                val dimArgCtx = functionCtx.functionExpr(1)?.expr()
                val dimension = if (dimArgCtx == null) 0 else {
                    (context.resolve(dimArgCtx)!!.value as SimpleValue).toBigInt()!!.toInt()
                }
                val width = functionCtx.functionExpr(0)?.expr()?.let { context.resolve(it) }?.value?.width
                    ?: error("Failed to get width of value passed to \$width().")
                val exprVerilog =
                    functionCtx.functionExpr(0)?.expr()?.verilog ?: error(ctx, "Missing value for ${function.label}!")
                when (width) {
                    is ArrayWidth -> {
                        buildString {
                            append("(")
                            var currentWidth = width
                            var currentDim = 0
                            while (true) {
                                if (currentDim == dimension) {
                                    when (currentWidth) {
                                        is DefinedSimpleWidth -> {
                                            append(currentWidth.size)
                                        }

                                        is DefinedArrayWidth -> {
                                            append(currentWidth.size)
                                        }

                                        is ResolvableWidth<*> -> {
                                            append(currentWidth.context.verilog)
                                        }

                                        else -> error("Unsupported type $currentWidth in evaluation of \$width()!")
                                    }
                                    break
                                }
                                when (currentWidth) {
                                    is SimpleWidth -> break
                                    is ArrayWidth -> currentWidth = currentWidth.next
                                    else -> error("Unsupported type $currentWidth in evaluation of \$width()!")
                                }
                                currentDim++
                            }
                            append(")")
                        }
                    }

                    is SimpleWidth -> {
                        "\$bits($exprVerilog)"
                    }

                    is StructWidth -> error("\$width() used on a struct!")
                }

            }

            Function.ASSERT,
            Function.PRINT,
            Function.TICK,
            Function.SILENTTICK,
            is Function.Custom ->
                error(ctx, "Test only function \"${function.label}\" can't be converted to Verilog!")

            Function.IS_SIMULATION ->
                error(ctx, "Function \"${function.label}\" result should always be constant but didn't have a value!")
        }
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

        val v = verilog[ctx.signal().requireNotNull(ctx)]
        if (v == null) {
            if (ctx.parent is LucidParser.RepeatStatContext)
                return

            val functionCtx = ctx.firstParentOrNull { it is FunctionContext }
            if (functionCtx is FunctionContext && functionCtx.FUNCTION_ID()?.text == "$" + Function.WIDTH.label)
                return
            error("Missing verilog for ${ctx::class.simpleName}: \"${ctx.text}\"")
        }

        ctx.verilog = v
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

        fun globalsToVerilog(globals: Collection<GlobalNamespace>) = buildString {
            var tabCount = 0

            fun StringBuilder.newLine(): StringBuilder {
                append("\n")
                repeat(tabCount) {
                    append("    ")
                }
                return this
            }

            fun SignalWidth.verilogArrayWidths(): List<String> {
                var current = this
                val widths = mutableListOf<String>()
                while (true) {
                    when (current) {
                        is BitWidth -> return widths
                        is DefinedArrayWidth -> {
                            widths.add("[${current.size - 1}:0]")
                            current = current.next
                        }

                        is DefinedSimpleWidth -> {
                            widths.add("[${current.size - 1}:0]")
                            return widths
                        }

                        is StructWidth -> return widths
                        is ResolvableWidth<*> -> error("Resolvable widths are not allowed in globals!")
                        is UndefinedWidth -> error("Undefined width during verilog conversion!")
                    }
                }
            }
            append(HEADER)
            newLine()
            append("package LucidGlobals;")
            tabCount++
            newLine()
            newLine()

            globals.forEach { global ->
                val prefix = "G_${global.name}_"
                global.constants.values.forEach { constant ->
                    append("localparam ")
                    constant.width.firstStructType()?.appendSpaceIfNotBlank()?.let { append(it) }
                    append(constant.width.verilogArrayWidths().surroundNameWithWidths("$prefix${constant.name}"))
                    append(" = ")
                    append(constant.initialValue.toVerilog())
                    append(";")
                    newLine()
                }

                global.structs.values.forEach { struct ->
                    append("typedef struct packed {")
                    tabCount++
                    struct.values.forEach { member ->
                        newLine()
                        append("logic ")
                        member.width.firstStructType()?.appendSpaceIfNotBlank()?.let { append(it) }
                        append(member.width.verilogArrayWidths().surroundNameWithWidths(member.name))
                        append(";")
                    }
                    tabCount--
                    newLine()
                    append("} ")
                    append(prefix)
                    append(struct.name)
                    append(";")
                    newLine()
                }
            }

            tabCount--
            newLine()
            append("endpackage")

        }
    }
}

fun List<String>.surroundNameWithWidths(name: String) = buildString {
    this@surroundNameWithWidths.lastOrNull()?.let {
        append(it)
        append(" ")
    }

    append(name)

    val unpackedDims = if (size > 1) subList(0, size - 1) else emptyList()
    if (unpackedDims.isNotEmpty())
        append(" ")
    unpackedDims.forEach {
        append(it)
    }
}

fun Value.toVerilog(): String {
    return when (this) {
        is ArrayValue -> "{{${elements.asReversed().joinToString(", ") { it.toVerilog() }}}}"
        is SimpleValue -> buildString {
            if (!width.isDefined())
                error("Can't convert undefined width value to Verilog!")
            append(width.bitCount)
            append("'")
            if (signed)
                append("s")
            if (isNumber()) {
                append("h")
                val bigInt = toBigInt()!!
                if (bigInt.signum() < 0) {
                    insert(0, "-")
                    append(bigInt.toString(16).replace("-", ""))
                } else {
                    append(bigInt.toString(16))
                }
            } else {
                append("b")
                bits.reversed().forEach {
                    append(it.char)
                }
            }
        }

        is StructValue -> "{{${type.values.joinToString(", ") { this[it.name]!!.toVerilog() }}}}"
        is UndefinedValue -> error("Can't convert undefined values to Verilog!")
    }
}

private fun SignalWidth.firstStructType(): String? {
    return when (this) {
        is StructWidth -> buildString {
            if (type.namespace != null) {
                append("LucidGlobals::G_")
                append(type.namespace)
                append("_")
            }
            append(type.name)
        }

        is ArrayWidth -> next.firstStructType()
        else -> null
    }
}

private fun String.appendSpaceIfNotBlank(): String = if (isNotBlank()) "$this " else this