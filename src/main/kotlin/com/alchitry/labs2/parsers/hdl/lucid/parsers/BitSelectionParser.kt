package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprContext
import com.alchitry.labs2.parsers.hdl.types.SelectionContext
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import com.alchitry.labs2.parsers.hdl.values.UndefinedValue
import com.alchitry.labs2.parsers.notations.*
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree

data class BitSelection(
    val range: IntRange,
    val ctx: ParserRuleContext,
    val selectionCtx: SelectionContext,
    val undefined: Boolean
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

    private fun setSkip(ctx: ParserRuleContext): Boolean {
        bounds[ctx] ?: return false
        val skip =
            ctx.children?.all { it !is ExprContext || context.resolve(it)?.type == ExprType.Constant }
                ?: return false
        if (skip)
            ctx.skip = true
        return skip
    }

    override fun exitBitSelection(ctx: BitSelectionContext) {
        if (ctx.children?.all { it.skip } == true) {
            ctx.skip = true
        }
    }

    /* Bounds Section */
    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        val expr = ctx.expr()
        if (expr.size != 2) return

        val maxCtx = ctx.expr(0) ?: return
        val minCtx = ctx.expr(1) ?: return

        val max = context.resolve(maxCtx) ?: return
        val min = context.resolve(minCtx) ?: return

        if (!max.type.known) context.reportExprNotConstant(expr[0])
        if (!min.type.known) context.reportExprNotConstant(expr[1])

        if (!context.checkSimpleWidth(*ctx.expr().toTypedArray()) {
                context.reportError(
                    it,
                    "The value used in bit selection must be a defined simple value (no arrays or structs)."
                )
            }) return

        if (min.value is UndefinedValue || max.value is UndefinedValue) {
            bounds[ctx] = BitSelection(0..0, ctx, SelectionContext.Fixed(minCtx, maxCtx), false)
            return
        }

        max.value as SimpleValue
        min.value as SimpleValue

        val maxNan = !max.value.isNumber()
        val minNan = !min.value.isNumber()
        if (maxNan) context.reportBitSelectorNotANumber(expr[0])
        if (minNan) context.reportBitSelectorNotANumber(expr[1])
        if (maxNan || minNan) return
        if (max.value.isLessThan(Bit.B0.toBitValue(true)) == min.value.isLessThan(Bit.B0.toBitValue(true))) {
            if (max.value.isLessThan(min.value).bit == Bit.B1) {
                context.reportBitSelectorOutOfOrder(ctx)
                return
            }
        }
        val maxInt: Int = try {
            max.value.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[0])
            return
        }
        val minInt: Int = try {
            min.value.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[1])
            return
        }
        bounds[ctx] = BitSelection(minInt..maxInt, ctx, SelectionContext.Fixed(minCtx, maxCtx), false)
        setSkip(ctx)
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        val expr = ctx.expr()
        if (expr.size != 2) return

        val start = context.resolve(expr[0]) ?: return
        val width = context.resolve(expr[1]) ?: return

        val isUpTo = ctx.getChild(2)?.text == "+"

        if (!width.type.known) {
            context.reportExprNotConstant(expr[1])
            return
        }

        if (!context.checkSimpleWidth(*expr.toTypedArray()) {
                context.reportError(
                    it,
                    "The value used in bit selection must be a simple value (no arrays or structs)."
                )
            }) return

        if (width.value is UndefinedValue) {
            val selectionContext = when (isUpTo) {
                true -> SelectionContext.UpTo(expr[0], expr[1])
                false -> SelectionContext.DownTo(expr[0], expr[1])
            }
            bounds[ctx] = BitSelection(0..0, ctx, selectionContext, undefined = true)
            return
        }

        width.value as SimpleValue

        if (!width.value.isNumber()) {
            context.reportBitSelectorNotANumber(expr[1])
            return
        }

        val widthInt = try {
            width.value.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr[1])
            return
        }

        if (widthInt <= 0) {
            context.reportBitSelectorZeroWidth(expr[1])
            return
        }

        val selectionContext = when (isUpTo) {
            true -> SelectionContext.UpTo(expr[0], expr[1])
            false -> SelectionContext.DownTo(expr[0], expr[1])
        }

        val startInt = if (start.value.isNumber()) {
            try {
                start.value as SimpleValue
                start.value.toBigInt()!!.intValueExact()
            } catch (e: ArithmeticException) {
                context.reportArraySizeTooBig(expr[0])
                return
            }
        } else {
            0
        }

        val selection = if (isUpTo)
            startInt until widthInt + startInt
        else
            (startInt - widthInt + 1)..startInt

        bounds[ctx] = BitSelection(selection, ctx, selectionContext, undefined = !start.value.isNumber())
        setSkip(ctx)
    }

    override fun exitArrayIndex(ctx: ArrayIndexContext) {
        val expr = ctx.expr() ?: return

        val index = context.resolve(expr) ?: return

        if (index.value is UndefinedValue) {
            bounds[ctx] = BitSelection(0..0, ctx, SelectionContext.Single(expr), undefined = true)
            return
        }

        if (index.value !is SimpleValue) {
            context.reportError(
                expr,
                "The value used as array index must be a defined simple value (no arrays or structs)."
            )
            return
        }

        if (!index.value.isNumber()) {
            bounds[ctx] = BitSelection(0..0, ctx, SelectionContext.Single(expr), undefined = true)
            return
        }

        val value = try {
            index.value.toBigInt()!!.intValueExact()
        } catch (e: ArithmeticException) {
            context.reportArraySizeTooBig(expr)
            return
        }

        bounds[ctx] = BitSelection(value..value, ctx, SelectionContext.Single(expr), undefined = false)
        setSkip(ctx)
    }
}