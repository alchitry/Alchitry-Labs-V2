package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs2.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs2.parsers.lucidv2.values.SimpleWidth
import com.alchitry.labs2.parsers.lucidv2.values.UndefinedWidth

/**
 * This will return true when all the expressions are flat. Aka they are all 1D arrays.
 *
 * The values themselves may be undefined.
 */
fun LucidExprContext.checkFlat(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
    return exprCtx.map {
        val op = resolve(it) ?: throw IllegalArgumentException("exprCtx wasn't defined")
        if (op.width is SimpleWidth) {
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
fun LucidExprContext.checkSimpleValue(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
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
fun LucidExprContext.checkFlatOrMatchingDims(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
    if (exprCtx.isEmpty())
        return true

    val first = resolve(exprCtx.first())?.width ?: throw IllegalArgumentException("exprCtx wasn't defined")

    return exprCtx.map {
        val op = resolve(it)?.width ?: throw IllegalArgumentException("exprCtx wasn't defined")
        if (!((op is SimpleWidth && first is SimpleWidth) || op == first)) {
            onError(it)
            return@map false
        }
        return@map true
    }.all { it }
}

/**
 * Checks if any widths are undefined and if so, flags any non-flat widths as errors
 */
fun LucidExprContext.checkUndefinedMatchingDims(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
    val widths =
        exprCtx.map { resolve(it)?.width ?: throw IllegalArgumentException("exprCtx wasn't defined") }
    val hasUndefinedWidth = widths.any { it is UndefinedWidth }
    if (!hasUndefinedWidth)
        return true

    return !widths.mapIndexed { index, signalWidth ->
        if (signalWidth !is SimpleWidth) {
            onError(exprCtx[index])
            true
        } else {
            false
        }
    }.any { it }
}