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
    private val verilog = mutableMapOf<ParseTree, String>()
    private var tabCount: Int = 0

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

    override fun exitSource(ctx: LucidParser.SourceContext) {
        ctx.verilog = buildString {
            append(HEADER)
            ctx.module().forEach { moduleContext ->
                newLine()
                newLine()
                append(moduleContext.verilog)
            }
        }
    }

    override fun enterModule(ctx: LucidParser.ModuleContext) {
        tabCount++
    }

    override fun exitModule(ctx: LucidParser.ModuleContext) {
        // TODO("Not yet implemented")
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
            val baseSignal = when (this) {
                is Signal -> this
                is SubSignal -> parent
            }

            return when (val parent = baseSignal.parent) {
                null -> baseSignal.name.sanitize()
                is Dff -> "D_${parent.name}_${baseSignal.name}"
                is EnumType -> "E_${parent.name}_${baseSignal.name}"
                is GlobalNamespace -> "G_${parent.name}_${baseSignal.name}"
                is ModuleInstanceOrArray -> "M_${parent.name}_${baseSignal.name}"
                is RepeatSignal -> "R_${parent.name}_${baseSignal.name}\""
            }
        }

    override fun exitSignal(ctx: LucidParser.SignalContext) {
        val signal = context.resolve(ctx) ?: error(ctx, "Failed to resolve signal for \"${ctx.text}\"")

        val baseSignal = when (signal) {
            is Signal -> signal
            is SubSignal -> signal.parent
        }

        val verilogName = signal.verilogName

        // signedness is lost when bits are selected and overwritten by structs
        var signed = baseSignal.signed

        // build the bit selection string (if sub-signal)
        val selection = (signal as? SubSignal)?.selection?.let { selection ->
            buildString {
                var currentWidth: SignalWidth? = baseSignal.width
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

                        UndefinedSimpleWidth -> error(ctx, "Undefined width during verilog conversion!")
                        null -> error(ctx, "Too many selectors for the signal width!")
                    }
                }

                if (selection.isNotEmpty() && selectedBitCount > 1) {
                    append("+")
                    append(selectedBitCount - 1)
                    append("-:")
                    append(selectedBitCount)
                    append("]")
                }
            }
        }

        val sigVerilog = "$verilogName$selection"
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