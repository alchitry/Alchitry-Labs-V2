package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.*
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree

data class BitSelection(
    val range: IntRange,
    val ctx: ParserRuleContext
)

/**
 * This class nests in ExprParse so that it is always run when ExprParser updates values
 */
open class BitSelectionParser(
    protected val context: LucidModuleContext
) : LucidBaseListener() {
    private val bounds = mutableMapOf<ParseTree, BitSelection>()

    fun resolve(ctx: BitSelectionContext): List<BitSelection> {
        return ctx.children.mapNotNull {
            bounds[it]
        }
    }

    private fun canSkip(ctx: ParserRuleContext): Boolean {
        bounds[ctx] ?: return false
        return ctx.children.filterIsInstance(ExprContext::class.java).all { context.expr.resolve(it)?.constant == true }
    }

    /* Bounds Section */
    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2) return

        val max = context.expr.resolve(ctx.expr(0)) ?: return
        val min = context.expr.resolve(ctx.expr(1)) ?: return

        if (!max.constant) context.errorCollector.reportExprNotConstant(ctx.expr(0))
        if (!min.constant) context.errorCollector.reportExprNotConstant(ctx.expr(1))

        if (!context.expr.checkSimpleValue(*ctx.expr().toTypedArray()) {
                context.errorCollector.reportBitSelectionNotSimpleValue(it)
            }) return

        max as SimpleValue
        min as SimpleValue

        val maxNan = !max.isNumber()
        val minNan = !min.isNumber()
        if (maxNan) context.errorCollector.reportBitSelectorNotANumber(ctx.expr(0))
        if (minNan) context.errorCollector.reportBitSelectorNotANumber(ctx.expr(1))
        if (maxNan || minNan) return
        if (max.isLessThan(min).bit == Bit.B1) {
            context.errorCollector.reportBitSelectorOutOfOrder(ctx)
            return
        }
        val maxInt: Int = try {
            max.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportArraySizeTooBig(ctx.expr(0))
            return
        }
        val minInt: Int = try {
            min.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportArraySizeTooBig(ctx.expr(1))
            return
        }
        bounds[ctx] = BitSelection(minInt..maxInt, ctx)
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2) return

        val start = context.expr.resolve(ctx.expr(0)) ?: return
        val width = context.expr.resolve(ctx.expr(1)) ?: return

        if (!width.constant) {
            context.errorCollector.reportExprNotConstant(ctx.expr(1))
            return
        }

        if (!context.expr.checkSimpleValue(*ctx.expr().toTypedArray()) {
                context.errorCollector.reportBitSelectionNotSimpleValue(it)
            }) return

        width as SimpleValue
        start as SimpleValue

        if (!width.isNumber()) {
            context.errorCollector.reportBitSelectorNotANumber(ctx.expr(1))
            return
        }

        if (!start.isNumber()) {
            context.errorCollector.reportBitSelectorNotANumber(ctx.expr(0))
            return
        }

        val widthInt = try {
            width.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportArraySizeTooBig(ctx.expr(1))
            return
        }

        val startInt = try {
            start.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportArraySizeTooBig(ctx.expr(0))
            return
        }

        if (widthInt <= 0) {
            context.errorCollector.reportBitSelectorZeroWidth(ctx.expr(1))
            return
        }

        val isUpTo = ctx.getChild(2).text == "+"

        val selection = if (isUpTo)
            startInt until widthInt + startInt
        else
            (startInt - widthInt + 1)..startInt

        bounds[ctx] = BitSelection(selection, ctx)
    }

    override fun exitArrayIndex(ctx: ArrayIndexContext) {
        if (canSkip(ctx)) return

        if (ctx.expr() == null) return

        val index = context.expr.resolve(ctx.expr()) ?: return

        if (index !is SimpleValue) {
            context.errorCollector.reportBitSelectionNotSimpleValue(ctx.expr())
            return
        }

        if (!index.isNumber()) {
            context.errorCollector.reportBitSelectorNotANumber(ctx.expr())
            return
        }

        val value = try {
            index.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportArraySizeTooBig(ctx.expr())
            return
        }

        bounds[ctx] = BitSelection(value..value, ctx)
    }
}