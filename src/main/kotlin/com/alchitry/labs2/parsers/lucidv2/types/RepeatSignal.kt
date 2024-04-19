package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.parsers.ExprType
import com.alchitry.labs2.parsers.lucidv2.values.DefinedSimpleWidth
import com.alchitry.labs2.parsers.lucidv2.values.UndefinedValue

class RepeatSignal(
    name: String,
    width: Int,
    context: LucidParser.RepeatStatContext,
) : SignalParent {
    val signal = Signal(
        name,
        SignalDirection.Read,
        this,
        UndefinedValue(DefinedSimpleWidth(width)),
        ExprType.Known
    )

    override fun getSignal(name: String): Signal? {
        if (signal.name == name)
            return signal
        return null
    }

    override val name: String = context.hashCode().toHexString(HexFormat.Default)
    override val parent: SignalParent? = null
}