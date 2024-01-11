package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.types.SelectionContext
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.notations.*
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree

data class BitSelection(
    val range: IntRange,
    val ctx: ParserRuleContext,
    val selectionCtx: SelectionContext
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
        return ctx.children?.mapNotNull {
            bounds[it]
        } ?: emptyList()
    }

    private fun canSkip(ctx: ParserRuleContext): Boolean {
        bounds[ctx] ?: return false
        return ctx.children?.filterIsInstance(ExprContext::class.java)?.all { context.resolve(it)?.constant == true }
            ?: return false
    }

    /* Bounds Section */
    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        if (canSkip(ctx)) return

        val expr = ctx.expr()
        if (expr.size != 2) return


        val maxCtx = ctx.expr(0) ?: return
        val minCtx = ctx.expr(1) ?: return

        val max = context.resolve(maxCtx) ?: return
        val min = context.resolve(minCtx) ?: return

        if (!max.constant) context.reportExprNotConstant(expr[0])
        if (!min.constant) context.reportExprNotConstant(expr[1])

        if (!context.checkSimpleValue(*ctx.expr().toTypedArray()) {
                context.reportBitSelectionNotSimpleValue(it)
            }) return

        max as SimpleValue
        min as SimpleValue

        val maxNan = !max.isNumber()
        val minNan = !min.isNumber()
        if (maxNan) context.reportBitSelectorNotANumber(expr[0])
        if (minNan) context.reportBitSelectorNotANumber(expr[1])
        if (maxNan || minNan) return
        if (max.isLessThan(min).bit == Bit.B1) {
            context.reportBitSelectorOutOfOrder(ctx)
            return
        }
        val maxInt: Int = try {
            max.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[0])
            return
        }
        val minInt: Int = try {
            min.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[1])
            return
        }
        bounds[ctx] = BitSelection(minInt..maxInt, ctx, SelectionContext.Fixed(minCtx, maxCtx))
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        if (canSkip(ctx)) return

        val expr = ctx.expr()
        if (expr.size != 2) return

        val start = context.resolve(expr[0]) ?: return
        val width = context.resolve(expr[1]) ?: return

        if (!width.constant) {
            context.reportExprNotConstant(expr[1])
            return
        }

        if (!context.checkSimpleValue(*expr.toTypedArray()) {
                context.reportBitSelectionNotSimpleValue(it)
            }) return

        width as SimpleValue
        start as SimpleValue

        if (!width.isNumber()) {
            context.reportBitSelectorNotANumber(expr[1])
            return
        }

        if (!start.isNumber()) {
            context.reportBitSelectorNotANumber(expr[0])
            return
        }

        val widthInt = try {
            width.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[1])
            return
        }

        val startInt = try {
            start.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[0])
            return
        }

        if (widthInt <= 0) {
            context.reportBitSelectorZeroWidth(expr[1])
            return
        }

        val isUpTo = ctx.getChild(2)?.text == "+"

        val selection = if (isUpTo)
            startInt until widthInt + startInt
        else
            (startInt - widthInt + 1)..startInt

        val selectionContext = when (isUpTo) {
            true -> SelectionContext.UpTo(expr[0], widthInt)
            false -> SelectionContext.DownTo(expr[0], widthInt)
        }

        bounds[ctx] = BitSelection(selection, ctx, selectionContext)
    }

    override fun exitArrayIndex(ctx: ArrayIndexContext) {
        if (canSkip(ctx)) return

        val expr = ctx.expr() ?: return

        val index = context.resolve(expr) ?: return

        if (index !is SimpleValue) {
            context.reportBitSelectionNotSimpleValue(expr)
            return
        }

        if (!index.isNumber()) {
            context.reportBitSelectorNotANumber(expr)
            return
        }

        val value = try {
            index.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr)
            return
        }

        bounds[ctx] = BitSelection(value..value, ctx, SelectionContext.Single(expr))
    }
}