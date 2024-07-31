package com.alchitry.labs2.parsers.hdl.lucid.context

import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import org.antlr.v4.kotlinruntime.ParserRuleContext

interface SignalResolver {
    fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent?
}