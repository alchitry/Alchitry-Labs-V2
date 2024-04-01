package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.BitUtil
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import com.alchitry.labs2.parsers.lucidv2.values.BitListWidth

data class EnumType(
    override val name: String,
    val members: Set<String>,
    override val parent: SignalParent?
) : SignalParent, Measurable {
    init {
        require(members.isNotEmpty()) { "EnumType members must contain at least one member!" }
    }

    override val width = BitListWidth(BitUtil.minWidthNum(members.size - 1))
    val memberSignals = members.mapIndexed { idx, name ->
        BitListValue(idx, width.size, constant = true, signed = false).asSignal(
            name,
            this
        )
    }.associateBy { it.name }

    override fun getSignal(name: String) = memberSignals[name]
}