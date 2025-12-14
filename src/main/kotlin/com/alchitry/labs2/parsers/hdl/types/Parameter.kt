package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.grammar.LucidParser.ExprContext
import com.alchitry.labs2.parsers.hdl.values.ArrayWidth
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.values.UndefinedArrayWidth
import com.alchitry.labs2.parsers.hdl.values.Value
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class Parameter(
    val name: String,
    val default: Value?,
    val defaultTestOnly: Boolean,
    val defaultExpr: ParserRuleContext?,
    val constraint: ExprContext?
) {
    companion object {
        fun widthsCompatible(defaultWidth: SignalWidth, valueWidth: SignalWidth): Boolean {
            val relaxedWidth = when (defaultWidth) {
                is ArrayWidth -> UndefinedArrayWidth(next = defaultWidth.next)
                else -> defaultWidth
            }
            return relaxedWidth.canAssign(valueWidth)
        }
    }
}
