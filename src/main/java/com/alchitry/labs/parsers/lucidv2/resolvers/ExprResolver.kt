package com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.UndefinedSimpleWidth
import com.alchitry.labs.parsers.lucidv2.values.Value

interface ExprResolver {
    fun resolve(ctx: ExprContext): Value?

    /**
     * This will return true when all the expressions are flat. Aka they are all 1D arrays.
     *
     * The values themselves may be undefined.
     */
    fun checkFlat(vararg exprCtx: ExprContext, onError: (ExprContext) -> Unit): Boolean {
        return exprCtx.map {
            val op = resolve(it) ?: throw IllegalArgumentException("exprCtx wasn't defined")
            if (op.signalWidth.isFlatArray()) {
                true
            } else {
                onError(it)
                false
            }
        }.all { it }
    }

    /**
     * This will return true when all expressions are SimpleValues.
     *
     * This differs from checkFlat in that the values may not be undefined.
     */
    fun checkSimpleValue(vararg exprCtx: ExprContext, onError: (ExprContext) -> Unit): Boolean {
        return exprCtx.map {
            val op = resolve(it) ?: throw IllegalArgumentException("exprCtx wasn't defined")
            if (op is SimpleValue) {
                true
            } else {
                onError(it)
                false
            }
        }.all { it }
    }

    /**
     * checks that all expressions have the same widths or are flat arrays
     */
    fun checkFlatOrMatchingDims(vararg exprCtx: ExprContext, onError: (ExprContext) -> Unit): Boolean {
        if (exprCtx.isEmpty())
            return true

        val first = resolve(exprCtx.first())?.signalWidth ?: throw IllegalArgumentException("exprCtx wasn't defined")

        return exprCtx.map {
            val op = resolve(it)?.signalWidth ?: throw IllegalArgumentException("exprCtx wasn't defined")
            if (!((op.isFlatArray() && first.isFlatArray()) || op == first)) {
                onError(it)
                return@map false
            }
            return@map true
        }.all { it }
    }

    /**
     * Checks if any widths are undefined and if so, flags any non-flat widths as errors
     */
    fun checkUndefinedMatchingDims(
        vararg exprCtx: ExprContext,
        onError: (ExprContext) -> Unit
    ): Boolean {
        val widths = exprCtx.map { resolve(it)?.signalWidth ?: throw IllegalArgumentException("exprCtx wasn't defined") }
        val hasUndefinedWidth = widths.any { it is UndefinedSimpleWidth }
        if (!hasUndefinedWidth)
            return true

        return !widths.mapIndexed { index, signalWidth ->
            if (!signalWidth.isFlatArray()) {
                onError(exprCtx[index])
                true
            } else {
                false
            }
        }.any { it }
    }
}