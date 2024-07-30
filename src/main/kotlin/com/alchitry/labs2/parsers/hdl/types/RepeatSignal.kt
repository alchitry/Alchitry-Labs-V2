package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.grammar.LucidParser

class RepeatSignal(
    val signal: Signal,
    context: LucidParser.RepeatStatContext,
) : SignalParent {
    override fun getSignal(name: String): Signal? {
        if (signal.name == name)
            return signal
        return null
    }

    override val name: String = context.hashCode().toHexString(HexFormat.Default)
    override val parent: SignalParent? = null
}