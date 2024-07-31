package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.hdl.ExprEvaluatorContext
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import com.alchitry.labs2.parsers.hdl.values.firstMostDefined
import org.antlr.v4.kotlinruntime.ParserRuleContext

/**
 * This will return true when all the expressions are flat. Aka they are all 1D arrays.
 *
 * The values themselves may be undefined.
 */
fun <T : ParserRuleContext> ExprEvaluatorContext<T>.checkSimpleWidth(
    vararg exprCtx: T,
    onError: (T) -> Unit
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
 * This will return true when all the expressions are flat. Aka they are all 1D arrays.
 *
 * The values themselves may be undefined.
 */
fun <T : ParserRuleContext> ExprEvaluatorContext<T>.checkSimpleWidth(
    exprCtx: Iterable<T>,
    onError: (T) -> Unit
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
fun <T : ParserRuleContext> ExprEvaluatorContext<T>.checkSimpleValue(
    vararg exprCtx: T,
    onError: (T) -> Unit
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
fun <T : ParserRuleContext> ExprEvaluatorContext<T>.checkSimpleOrCompatible(
    vararg exprCtx: T,
    onError: (T) -> Unit
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

/**
 * checks that all expressions have the same widths or are flat arrays
 */
fun <T : ParserRuleContext> ExprEvaluatorContext<T>.checkSimpleOrCompatible(
    exprCtx: Collection<T>,
    onError: (T) -> Unit
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