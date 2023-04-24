package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.*
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
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
    private val errorListener: ErrorListener = dummyErrorListener,
    private val resolver: LucidParseContext
) : LucidBaseListener() {
    private val bounds = mutableMapOf<ParseTree, BitSelection>()

    fun resolve(ctx: BitSelectionContext): List<BitSelection> {
        return ctx.children.mapNotNull {
            bounds[it]
        }
    }

    private fun canSkip(ctx: ParserRuleContext): Boolean {
        bounds[ctx] ?: return false
        return ctx.children.filterIsInstance(ExprContext::class.java).all { resolver.expr.resolve(it)?.constant == true }
    }

    /* Bounds Section */
    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2) return

        val max = resolver.expr.resolve(ctx.expr(0)) ?: return
        val min = resolver.expr.resolve(ctx.expr(1)) ?: return

        if (!max.constant) errorListener.reportExprNotConstant(ctx.expr(0))
        if (!min.constant) errorListener.reportExprNotConstant(ctx.expr(1))

        if (!resolver.expr.checkSimpleValue(*ctx.expr().toTypedArray()) {
                errorListener.reportBitSelectionNotSimpleValue(it)
            }) return

        max as BitListValue
        min as BitListValue

        val maxNan = !max.isNumber()
        val minNan = !min.isNumber()
        if (maxNan) errorListener.reportBitSelectorNotANumber(ctx.expr(0))
        if (minNan) errorListener.reportBitSelectorNotANumber(ctx.expr(1))
        if (maxNan || minNan) return
        if (max.isLessThan(min).toBoolean()) {
            errorListener.reportBitSelectorOutOfOrder(ctx)
            return
        }
        val maxInt: Int = try {
            max.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            errorListener.reportArraySizeTooBig(ctx.expr(0))
            return
        }
        val minInt: Int = try {
            min.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            errorListener.reportArraySizeTooBig(ctx.expr(1))
            return
        }
        bounds[ctx] = BitSelection(minInt..maxInt, ctx)
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2) return

        val start = resolver.expr.resolve(ctx.expr(0)) ?: return
        val width = resolver.expr.resolve(ctx.expr(1)) ?: return

        if (!width.constant) {
            errorListener.reportExprNotConstant(ctx.expr(1))
            return
        }

        if (!resolver.expr.checkSimpleValue(*ctx.expr().toTypedArray()) {
                errorListener.reportBitSelectionNotSimpleValue(it)
            }) return

        width as BitListValue
        start as BitListValue

        if (!width.isNumber()) {
            errorListener.reportBitSelectorNotANumber(ctx.expr(1))
            return
        }

        if (!start.isNumber()) {
            errorListener.reportBitSelectorNotANumber(ctx.expr(0))
            return
        }

        val widthInt = try {
            width.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            errorListener.reportArraySizeTooBig(ctx.expr(1))
            return
        }

        val startInt = try {
            start.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            errorListener.reportArraySizeTooBig(ctx.expr(0))
            return
        }

        if (widthInt <= 0) {
            errorListener.reportBitSelectorZeroWidth(ctx.expr(1))
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

        val index = resolver.expr.resolve(ctx.expr()) ?: return

        if (index !is BitListValue) {
            errorListener.reportBitSelectionNotSimpleValue(ctx.expr())
            return
        }

        if (!index.isNumber()) {
            errorListener.reportBitSelectorNotANumber(ctx.expr())
            return
        }

        val value = try {
            index.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            errorListener.reportArraySizeTooBig(ctx.expr())
            return
        }

        bounds[ctx] = BitSelection(value..value, ctx)
    }
}