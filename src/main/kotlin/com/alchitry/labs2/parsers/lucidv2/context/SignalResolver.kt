package com.alchitry.labs2.parsers.lucidv2.context

import com.alchitry.labs2.parsers.lucidv2.types.SignalOrParent
import org.antlr.v4.kotlinruntime.ParserRuleContext

interface SignalResolver {
    fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent?
}