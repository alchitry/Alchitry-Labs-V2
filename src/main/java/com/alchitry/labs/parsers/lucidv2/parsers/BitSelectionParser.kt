package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.*
import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
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
data class BitSelectionParser(
    private val context: LucidExprContext,
    private val bounds: MutableMap<ParseTree, BitSelection> = mutableMapOf()
) : LucidBaseListener() {
    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolve(ctx: BitSelectionContext): List<BitSelection> {
        return ctx.children.mapNotNull {
            bounds[it]
        }
    }

    private fun canSkip(ctx: ParserRuleContext): Boolean {
        bounds[ctx] ?: return false
        return ctx.children.filterIsInstance(ExprContext::class.java).all { context.resolve(it)?.constant == true }
    }

    /* Bounds Section */
    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2) return

        val max = context.resolve(ctx.expr(0)) ?: return
        val min = context.resolve(ctx.expr(1)) ?: return

        if (!max.constant) context.reportExprNotConstant(ctx.expr(0))
        if (!min.constant) context.reportExprNotConstant(ctx.expr(1))

        if (!context.checkSimpleValue(*ctx.expr().toTypedArray()) {
                context.reportBitSelectionNotSimpleValue(it)
            }) return

        max as SimpleValue
        min as SimpleValue

        val maxNan = !max.isNumber()
        val minNan = !min.isNumber()
        if (maxNan) context.reportBitSelectorNotANumber(ctx.expr(0))
        if (minNan) context.reportBitSelectorNotANumber(ctx.expr(1))
        if (maxNan || minNan) return
        if (max.isLessThan(min).bit == Bit.B1) {
            context.reportBitSelectorOutOfOrder(ctx)
            return
        }
        val maxInt: Int = try {
            max.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(ctx.expr(0))
            return
        }
        val minInt: Int = try {
            min.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(ctx.expr(1))
            return
        }
        bounds[ctx] = BitSelection(minInt..maxInt, ctx)
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2) return

        val start = context.resolve(ctx.expr(0)) ?: return
        val width = context.resolve(ctx.expr(1)) ?: return

        if (!width.constant) {
            context.reportExprNotConstant(ctx.expr(1))
            return
        }

        if (!context.checkSimpleValue(*ctx.expr().toTypedArray()) {
                context.reportBitSelectionNotSimpleValue(it)
            }) return

        width as SimpleValue
        start as SimpleValue

        if (!width.isNumber()) {
            context.reportBitSelectorNotANumber(ctx.expr(1))
            return
        }

        if (!start.isNumber()) {
            context.reportBitSelectorNotANumber(ctx.expr(0))
            return
        }

        val widthInt = try {
            width.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(ctx.expr(1))
            return
        }

        val startInt = try {
            start.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(ctx.expr(0))
            return
        }

        if (widthInt <= 0) {
            context.reportBitSelectorZeroWidth(ctx.expr(1))
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

        val index = context.resolve(ctx.expr()) ?: return

        if (index !is SimpleValue) {
            context.reportBitSelectionNotSimpleValue(ctx.expr())
            return
        }

        if (!index.isNumber()) {
            context.reportBitSelectorNotANumber(ctx.expr())
            return
        }

        val value = try {
            index.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(ctx.expr())
            return
        }

        bounds[ctx] = BitSelection(value..value, ctx)
    }
}