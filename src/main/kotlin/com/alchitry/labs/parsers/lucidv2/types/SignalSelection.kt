package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.grammar.LucidParser

typealias SignalSelection = List<SignalSelector>

class SignalSelectionException(val selector: SignalSelector, message: String) : Exception(message)

sealed class SignalSelector {
    data class Bits(val range: IntRange, val context: SelectionContext) : SignalSelector() {
        constructor(bit: Int, context: SelectionContext) : this(bit..bit, context)

        override fun toString(): String {
            return if (range.first == range.last) {
                "[${range.first}]"
            } else {
                "[${range.last}:${range.first}]"
            }
        }
    }

    data class Struct(val member: String) : SignalSelector() {
        override fun toString(): String {
            return member
        }
    }
}

sealed class SelectionContext {
    data class Constant(
        val start: Int,
        val stop: Int
    ) : SelectionContext() {
        constructor(bit: Int) : this(bit, bit)
    }

    data class Single(
        val bit: LucidParser.ExprContext
    ) : SelectionContext()

    data class Fixed(
        val start: LucidParser.ExprContext,
        val stop: LucidParser.ExprContext
    ) : SelectionContext()

    data class DownTo(
        val start: LucidParser.ExprContext,
        val width: Int
    ) : SelectionContext()

    data class UpTo(
        val start: LucidParser.ExprContext,
        val width: Int
    ) : SelectionContext()
}