package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.BitSelectorConstContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext

object Errors {
    fun exprNotConstant(expression: String) = "The expression \"$expression\" must be constant."

    fun arraySizeTooBig(dimension: String) = "The array dimension \"$dimension\" is too big."

    fun bitSelectorNotSimpleValue() =
        "The value used in bit selection must be a defined simple value (no arrays or structs)."

    fun bitSelectorNotANumber() = "Bit selectors must be a number (no x or z values)."
    fun bitSelectorOutOfOrder() = "The value on the left must be greater or equal to the value on the right."
    fun bitSelectorZeroWidth() = "Bit selectors must select at least one bit."
}

fun ErrorListener.reportExprNotConstant(expr: ExprContext) = reportError(expr, Errors.exprNotConstant(expr.text))

fun ErrorListener.reportArraySizeTooBig(expr: ExprContext) = reportError(expr, Errors.arraySizeTooBig(expr.text))

fun ErrorListener.reportBitSelectionNotSimpleValue(expr: ExprContext) =
    reportError(expr, Errors.bitSelectorNotSimpleValue())

fun ErrorListener.reportBitSelectorNotANumber(expr: ExprContext) = reportError(expr, Errors.bitSelectorNotANumber())
fun ErrorListener.reportBitSelectorOutOfOrder(selector: BitSelectorConstContext) =
    reportError(selector, Errors.bitSelectorOutOfOrder())

fun ErrorListener.reportBitSelectorZeroWidth(expr: ExprContext) = reportError(expr, Errors.bitSelectorZeroWidth())