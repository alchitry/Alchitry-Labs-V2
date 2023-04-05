package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers.BitSelectionResolver
import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers.LucidResolver
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.*
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import org.antlr.v4.runtime.tree.ParseTree

class BitSelectionParser(
    private val errorListener: ErrorListener = dummyErrorListener,
    private val resolver: LucidResolver
) : LucidBaseListener(), BitSelectionResolver {
    private val bounds = mutableMapOf<ParseTree, IntRange>()

    init {
        resolver.bitSelection = this
    }

    override fun resolve(ctx: Bit_selectionContext): List<IntRange> {
        return ctx.children.mapNotNull {
            bounds[it]
        }
    }

    /* Bounds Section */
    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        if (ctx.expr().size == 2) {
            val max = resolver.expr.resolve(ctx.expr(0)) ?: return
            val min = resolver.expr.resolve(ctx.expr(1)) ?: return

            if (!max.constant) errorListener.reportExprNotConstant(ctx.expr(0))
            if (!min.constant) errorListener.reportExprNotConstant(ctx.expr(1))

            if (!resolver.expr.checkSimpleValue(*ctx.expr().toTypedArray()) {
                    errorListener.reportBitSelectionNotSimpleValue(it)
                }) return

            max as SimpleValue
            min as SimpleValue

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
            bounds[ctx] = minInt..maxInt
        }
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
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

        width as SimpleValue
        start as SimpleValue

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

        bounds[ctx] = if (isUpTo)
            startInt until widthInt + startInt
        else
            (startInt - widthInt + 1)..startInt
    }

    override fun exitArray_index(ctx: Array_indexContext) {
        if (ctx.expr() == null) return

        val index = resolver.expr.resolve(ctx.expr()) ?: return

        if (index !is SimpleValue) {
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

        bounds[ctx] = value..value
    }
}