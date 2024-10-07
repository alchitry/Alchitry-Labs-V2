package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.parsers.RepeatBlock
import com.alchitry.labs2.parsers.hdl.values.Value

class RepeatSignal(
    name: String,
    initialValue: Value,
    context: LucidParser.RepeatStatContext,
    val block: RepeatBlock
) : SignalParent {
    val signal = Signal(
        name,
        SignalDirection.Read,
        this,
        initialValue,
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