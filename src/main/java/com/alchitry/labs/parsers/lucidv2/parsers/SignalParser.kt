package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers.ExprResolver
import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers.LucidResolver
import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers.StructResolver
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.Signal

class SignalParser(
    private val errorListener: ErrorListener = dummyErrorListener,
    private val resolver: LucidResolver
) : LucidBaseListener(), SignalResolver {
    private val dffs = mutableMapOf<String, Signal>()

    init {
        resolver.signal = this
    }

    override fun resolve(name: String): Signal? {
        dffs[name]?.let { return it }

        return null // TODO
    }

    override fun exitDffDec(ctx: LucidParser.DffDecContext) {
        val isSigned = ctx.SIGNED() != null


    }
}