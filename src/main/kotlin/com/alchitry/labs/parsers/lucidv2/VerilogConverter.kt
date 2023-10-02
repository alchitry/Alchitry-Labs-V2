package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.NotationType
import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.types.*
import com.alchitry.labs.parsers.lucidv2.values.*
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.tree.ParseTree

class VerilogConverter(
    private val context: LucidBlockContext
) : LucidBaseListener() {
    private val verilog = mutableMapOf<ParseTree, String>()
    private fun error(ctx: ParserRuleContext, message: String): Nothing {
        val notation = ErrorListener.getNotation(ctx, message, NotationType.Error)
        error(notation.toString())
    }

    private fun error(token: Token, message: String): Nothing {
        val notation = ErrorListener.getNotation(token, message, NotationType.Error)
        error(notation.toString())
    }

    /**
     * Checks if the expression is constant and can be replaced by a simple value.
     * @return true if the expression was handled, false otherwise
     */
    private fun handleConstant(ctx: LucidParser.ExprContext): Boolean {
        val value = context.resolve(ctx) ?: return false
        if (value.constant) {
            ctx.verilog = value.flatten().asVerilog()
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
     * Throws an [IllegalStateException] if the value is null with a message containing the location of [parent].
     * @param parent The context to use as the error location if this is null.
     */
    private inline fun <reified T : ParseTree?> T.requireNotNull(parent: ParserRuleContext): ParseTree {
        return this ?: error(parent, "Context \"${T::class.simpleName}\" was null!")
    }

    override fun exitSource(ctx: LucidParser.SourceContext) {
        // TODO("Not yet implemented")
    }

    override fun exitModule(ctx: LucidParser.ModuleContext) {
        // TODO("Not yet implemented")
    }

    override fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {
        // TODO("Not yet implemented")
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

    override fun exitSignal(ctx: LucidParser.SignalContext) {
        val signal = context.resolve(ctx) ?: error(ctx, "Failed to resolve signal for \"${ctx.text}\"")

        val baseSignal = when (signal) {
            is Signal -> signal
            is SubSignal -> signal.parent
        }

        val baseName = when (val parent = baseSignal.parent) {
            null -> baseSignal.name.sanitize()
            is Dff -> "D_${parent.name}_${baseSignal.name}"
            is EnumType -> "E_${parent.name}_${baseSignal.name}"
            is GlobalNamespace -> "G_${parent.name}_${baseSignal.name}"
            is ModuleInstanceOrArray -> "M_${parent.name}_${baseSignal.name}"
        }

        // signedness is lost when bits are selected and overwritten by structs
        var signed = baseSignal.signed

        // build the bit selection string (if sub-signal)
        val selection = (signal as? SubSignal)?.selection?.let { selection ->
            val bitSelection = StringBuilder()
            var currentWidth: SignalWidth? = baseSignal.width
            check(currentWidth?.isDefined() == true) { "Signal width was not defined!" }

            var selectedBitCount = currentWidth!!.bitCount!! // the defined check above makes this safe

            var first = true

            if (selection.isNotEmpty())
                bitSelection.append("[")

            for (selector in selection) {
                if (!first)
                    bitSelection.append("+")
                else
                    first = false

                when (currentWidth) {
                    is ArrayWidth, is SimpleWidth -> {
                        val s = selector as? SignalSelector.Bits ?: error("Struct selector used on an array!")

                        bitSelection.append("(")
                        when (s.context) {
                            is SelectionContext.Constant -> bitSelection.append(s.range.first)
                            is SelectionContext.Single -> bitSelection.append(s.context.bit.verilog)
                            is SelectionContext.Fixed -> bitSelection.append(s.context.start.verilog)
                            is SelectionContext.DownTo -> bitSelection.append("(${s.context.stop.verilog})-${s.context.width - 1}")
                            is SelectionContext.UpTo -> bitSelection.append(s.context.start)
                        }
                        bitSelection.append(")")

                        val elementSize = if (currentWidth is ArrayWidth) currentWidth.next.bitCount!! else 1

                        // scale the offset by the size of each element
                        if (elementSize > 1)
                            bitSelection
                                .append("*")
                                .append(elementSize)

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

                        bitSelection.append(type.offsetOf(s.member))

                        signed = member.signed
                        currentWidth = member.width
                        selectedBitCount = currentWidth.bitCount!!
                    }

                    UndefinedSimpleWidth -> error(ctx, "Undefined width during verilog conversion!")
                    null -> error(ctx, "Too many selectors for the signal width!")
                }
            }

            if (selection.isNotEmpty() && selectedBitCount > 1) {
                bitSelection
                    .append("+")
                    .append(selectedBitCount - 1)
                    .append("-:")
                    .append(selectedBitCount)
                    .append("]")
            }

            bitSelection.toString()
        }

        val sigVerilog = "$baseName$selection"
        val write = ctx.parent is LucidParser.AssignStatContext
        ctx.verilog = if (signed && !write) "\$signed($sigVerilog)" else sigVerilog
    }

    override fun exitCaseStat(ctx: LucidParser.CaseStatContext) {
        // TODO("Not yet implemented")
    }

    override fun exitCaseElem(ctx: LucidParser.CaseElemContext) {
        // TODO("Not yet implemented")
    }

    override fun exitCaseBlock(ctx: LucidParser.CaseBlockContext) {
        // TODO("Not yet implemented")
    }

    override fun exitIfStat(ctx: LucidParser.IfStatContext) {
        // TODO("Not yet implemented")
    }

    override fun exitElseStat(ctx: LucidParser.ElseStatContext) {
        // TODO("Not yet implemented")
    }

    override fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) {
        // TODO("Not yet implemented")
    }

    override fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext) {
        // TODO("Not yet implemented")
    }

    override fun exitExprTernary(ctx: LucidParser.ExprTernaryContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
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
        // TODO("Not yet implemented")
    }

    override fun exitExprInvert(ctx: LucidParser.ExprInvertContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprStruct(ctx: LucidParser.ExprStructContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprArray(ctx: LucidParser.ExprArrayContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprShift(ctx: LucidParser.ExprShiftContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprLogical(ctx: LucidParser.ExprLogicalContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprNegate(ctx: LucidParser.ExprNegateContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprGroup(ctx: LucidParser.ExprGroupContext) {
        if (handleConstant(ctx))
            return
        ctx.verilog = "(${ctx.expr().requireNotNull(ctx).verilog})"
    }

    override fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprFunction(ctx: LucidParser.ExprFunctionContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprCompare(ctx: LucidParser.ExprCompareContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprDup(ctx: LucidParser.ExprDupContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
    }

    override fun exitExprSignal(ctx: LucidParser.ExprSignalContext) {
        if (handleConstant(ctx))
            return
        // TODO("Not yet implemented")
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

    }
}