package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.lucidv2.parsers.ExprType
import com.alchitry.labs2.parsers.lucidv2.values.Value
import org.antlr.v4.kotlinruntime.ParserRuleContext

class LocalSignal(
    name: String,
    initValue: Value,
    signed: Boolean,
    val scope: ParserRuleContext
) : SignalParent {
    val signal = Signal(
        name,
        SignalDirection.Both,
        this,
        initValue,
        ExprType.Dynamic,
        signed
    )

    override fun getSignal(name: String): Signal? {
        if (signal.name == name)
            return signal
        return null
    }

    override val name: String = scope.hashCode().toHexString(HexFormat.Default)
    override val parent: SignalParent? = null
}