package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs2.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs2.parsers.lucidv2.values.firstMostDefined

/**
 * This will return true when all the expressions are flat. Aka they are all 1D arrays.
 *
 * The values themselves may be undefined.
 */
fun LucidExprContext.checkSimpleWidth(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
    return exprCtx.map {
        val op = resolve(it)?.value ?: throw IllegalArgumentException("exprCtx wasn't defined")
        if (op.width.isSimple()) {
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
 * This differs from checkSimpleWidth in that the values may not be undefined.
 */
fun LucidExprContext.checkSimpleValue(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
    return exprCtx.map {
        val op = resolve(it)?.value ?: throw IllegalArgumentException("exprCtx wasn't defined")
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
fun LucidExprContext.checkSimpleOrCompatible(
    vararg exprCtx: LucidParser.ExprContext,
    onError: (LucidParser.ExprContext) -> Unit
): Boolean {
    if (exprCtx.isEmpty())
        return true

    val widths = exprCtx.map { resolve(it)?.value?.width ?: throw IllegalArgumentException("exprCtx wasn't defined") }
    val first = widths.firstMostDefined()

    return exprCtx.map {
        val op = resolve(it)?.value?.width ?: throw IllegalArgumentException("exprCtx wasn't defined")
        if (!(op.isCompatibleWith(first) || (op.isSimple() && first.isSimple()))) {
            onError(it)
            return@map false
        }
        return@map true
    }.all { it }
}