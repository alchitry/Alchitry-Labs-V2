package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.grammar.LucidParser

typealias SignalSelection = List<SignalSelector>

class SignalSelectionException(val selector: SignalSelector, message: String) : Exception(message)

fun SignalSelection.isUndefined(): Boolean = any {
    it is SignalSelector.Bits && it.undefined
}

fun SignalSelection.overlaps(other: SignalSelection): Boolean {
    val minDim = size.coerceAtMost(other.size)
    for (i in 0..<minDim) {
        if (!this[i].overlaps(other[i]))
            return false
    }
    return true
}

sealed class SignalSelector {
    abstract fun overlaps(other: SignalSelector): Boolean

    data class Bits(
        val range: IntRange,
        val context: SelectionContext,
        val undefined: Boolean = false,
        val single: Boolean = context is SelectionContext.Single
    ) : SignalSelector() {
        constructor(bit: Int, undefined: Boolean = false) : this(
            bit..bit,
            SelectionContext.Constant,
            undefined,
            single = true
        )

        /**
         * The number of elements selected
         */
        val count: Int get() = range.last - range.first + 1

        override fun toString(): String {
            return if (range.first == range.last) {
                "[${range.first}]"
            } else {
                "[${range.last}:${range.first}]"
            }
        }

        override fun overlaps(other: SignalSelector): Boolean =
            when (other) {
                is Bits -> range.first <= other.range.last && other.range.first <= range.last
                else -> false
            }

    }

    data class Struct(val member: String) : SignalSelector() {
        override fun toString(): String {
            return ".$member"
        }

        override fun overlaps(other: SignalSelector): Boolean =
            when (other) {
                is Struct -> member == other.member
                else -> false
            }
    }
}

sealed class SelectionContext {
    data object Constant : SelectionContext()

    data class Single(
        val bit: LucidParser.ExprContext
    ) : SelectionContext()

    data class Fixed(
        val start: LucidParser.ExprContext,
        val stop: LucidParser.ExprContext
    ) : SelectionContext()

    data class DownTo(
        val stop: LucidParser.ExprContext,
        val width: LucidParser.ExprContext
    ) : SelectionContext()

    data class UpTo(
        val start: LucidParser.ExprContext,
        val width: LucidParser.ExprContext
    ) : SelectionContext()
}